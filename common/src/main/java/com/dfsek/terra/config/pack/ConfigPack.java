package com.dfsek.terra.config.pack;

import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.tectonic.abstraction.AbstractConfigLoader;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.LoaderRegistrar;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.events.config.ConfigPackPostLoadEvent;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.Registry;
import com.dfsek.terra.api.structures.loot.LootTable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.generic.pair.ImmutablePair;
import com.dfsek.terra.api.util.seeded.NoiseProvider;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.fileloaders.Loader;
import com.dfsek.terra.config.loaders.config.BufferedImageLoader;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.BiomePipelineTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.ImageProviderTemplate;
import com.dfsek.terra.config.loaders.config.biome.templates.provider.SingleBiomeProviderTemplate;
import com.dfsek.terra.config.loaders.config.sampler.NoiseSamplerBuilderLoader;
import com.dfsek.terra.config.loaders.config.sampler.templates.ImageSamplerTemplate;
import com.dfsek.terra.config.prototype.ConfigType;
import com.dfsek.terra.config.prototype.ProtoConfig;
import com.dfsek.terra.registry.OpenRegistry;
import com.dfsek.terra.registry.config.ConfigTypeRegistry;
import com.dfsek.terra.registry.config.FunctionRegistry;
import com.dfsek.terra.registry.config.LootRegistry;
import com.dfsek.terra.registry.config.NoiseRegistry;
import com.dfsek.terra.registry.config.ScriptRegistry;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.items.TerraStructure;
import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Represents a Terra configuration pack.
 */
public class ConfigPack implements LoaderRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final TerraPlugin main;
    private final Loader loader;
    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();
    private final ConfigLoader selfLoader = new ConfigLoader();
    private final Scope varScope = new Scope();
    private final BiomeProvider.BiomeProviderBuilder biomeProviderBuilder;
    
    private final ConfigPackTemplate template = new ConfigPackTemplate();
    
    
    private final ConfigTypeRegistry configTypeRegistry;
    private final Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> registryMap = newRegistryMap();
    
    public ConfigPack(TerraPlugin main, Loader loader) throws ConfigException, IOException {
        this.configTypeRegistry = new ConfigTypeRegistry(this, main, (id, configType) -> {
            OpenRegistry<?> openRegistry = configType.registrySupplier().get();
            registryMap.put(configType.getTypeClass(), ImmutablePair.of(openRegistry, new CheckedRegistry<>(openRegistry)));
        });
        this.loader = loader;
        this.main = main;
        
        long startTime = System.nanoTime();
        
        register(selfLoader);
        main.register(selfLoader);
        register(abstractConfigLoader);
        main.register(abstractConfigLoader);
        
        selfLoader.load(template, loader.get("pack.yml"));
        
        logger.info("pack.yml is valid for config pack '{}'. Attempting to load all entities.", template.getID());
        
        load(startTime, main);
        
        ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate(); // Constructing dummy provider to catch errors at load time.
        selfLoader.load(packPostTemplate, loader.get("pack.yml"));
        biomeProviderBuilder = packPostTemplate.getProviderBuilder();
        biomeProviderBuilder.build(0); // Build dummy provider to catch errors at load time.
        toWorldConfig(new TerraWorld(new DummyWorld(), this, main)); // Build now to catch any errors immediately.
        
        checkDeadEntries();
    }
    
    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(ConfigType.class, configTypeRegistry)
                .registerLoader(BufferedImage.class, new BufferedImageLoader(loader))
                .registerLoader(SingleBiomeProviderTemplate.class, SingleBiomeProviderTemplate::new)
                .registerLoader(BiomePipelineTemplate.class, () -> new BiomePipelineTemplate(main))
                .registerLoader(ImageProviderTemplate.class, () -> new ImageProviderTemplate(getRegistry(BiomeBuilder.class)))
                .registerLoader(ImageSamplerTemplate.class, () -> new ImageProviderTemplate(getRegistry(BiomeBuilder.class)))
                .registerLoader(NoiseSeeded.class, new NoiseSamplerBuilderLoader(getOpenRegistry(NoiseProvider.class)));
    }
    
    private <R> void putPair(Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> map, Class<R> key, OpenRegistry<R> l) {
        map.put(key, ImmutablePair.of(l, new CheckedRegistry<>(l)));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> newRegistryMap() {
        Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> map =
                new HashMap<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>>() {
                    private static final long serialVersionUID = 4015855819914064466L;
                    
                    @Override
                    public ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>> put(Class<?> key,
                                                                                  ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>> value) {
                        selfLoader.registerLoader(key, value.getLeft());
                        abstractConfigLoader.registerLoader(key, value.getLeft());
                        return super.put(key, value);
                    }
                };
        
        putPair(map, NoiseProvider.class, new NoiseRegistry());
        putPair(map, FunctionBuilder.class, (OpenRegistry<FunctionBuilder>) (Object) new FunctionRegistry());
        putPair(map, LootTable.class, new LootRegistry());
        putPair(map, StructureScript.class, new ScriptRegistry());
        
        return map;
    }
    
    protected Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> getRegistryMap() {
        return registryMap;
    }
    
    private void checkDeadEntries() {
        registryMap.forEach((clazz, pair) -> pair.getLeft()
                                                 .getDeadEntries()
                                                 .forEach((id, value) -> logger.warn("Dead entry in '{}' registry: '{}'", clazz, id)));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void load(long startTime, TerraPlugin main) throws ConfigException {
        main.getEventManager().callEvent(new ConfigPackPreLoadEvent(this));
        
        for(Map.Entry<String, Double> var : template.getVariables().entrySet()) {
            varScope.create(var.getKey(), var.getValue());
        }
        
        loader.open("", ".tesf").thenEntries(entries -> {
            for(Map.Entry<String, InputStream> entry : entries) {
                try(InputStream stream = entry.getValue()) {
                    StructureScript structureScript = new StructureScript(stream, main, getRegistry(StructureScript.class),
                                                                          getRegistry(LootTable.class),
                                                                          (Registry<FunctionBuilder<?>>) (Object) getRegistry(
                                                                                  FunctionBuilder.class));
                    getOpenRegistry(StructureScript.class).add(structureScript.getId(), structureScript);
                } catch(com.dfsek.terra.api.structures.parser.exceptions.ParseException | IOException e) {
                    throw new LoadException("Unable to load script \"" + entry.getKey() + "\"", e);
                }
            }
            
            logger.info("Loaded {} structures for pack {}.", entries.size(), template.getID());
        }).close();
        
        loader.open("structures/loot", ".json").thenEntries(entries -> {
            for(Map.Entry<String, InputStream> entry : entries) {
                try {
                    getOpenRegistry(LootTable.class).add(entry.getKey(),
                                                         new LootTable(IOUtils.toString(entry.getValue(), StandardCharsets.UTF_8), main));
                } catch(ParseException | IOException | NullPointerException e) {
                    throw new LoadException("Unable to load loot table \"" + entry.getKey() + "\"", e);
                }
            }
            logger.info("Loaded {} loot files for pack {}.", entries.size(), template.getID());
        }).close();
        
        List<Configuration> configurations = new ArrayList<>();
        
        loader.open("", ".yml").thenEntries(entries -> {
            entries.forEach(stream -> configurations.add(new Configuration(stream.getValue(), stream.getKey())));
            logger.info("Loaded {} config files for pack {}.", entries.size(), template.getID());
        });
        
        Map<ConfigType<? extends ConfigTemplate, ?>, List<Configuration>> configs = new HashMap<>();
        
        for(Configuration configuration : configurations) {
            ProtoConfig config = new ProtoConfig();
            selfLoader.load(config, configuration);
            configs.computeIfAbsent(config.getType(), configType -> new ArrayList<>()).add(configuration);
        }
        
        for(ConfigType<?, ?> configType : configTypeRegistry.entries()) {
            for(ConfigTemplate config : abstractConfigLoader.loadConfigs(configs.getOrDefault(configType, Collections.emptyList()),
                                                                         () -> configType.getTemplate(this, main))) {
                ((ConfigType) configType).callback(this, main, config);
            }
        }
        
        main.getEventManager().callEvent(new ConfigPackPostLoadEvent(this));
        
        logger.info("Loaded config pack '{}' v{} by {} in {}ms.", template.getID(), template.getVersion(), template.getAuthor(),
                    (System.nanoTime() - startTime) / 1000000D);
    }
    
    @SuppressWarnings("unchecked")
    protected <T> OpenRegistry<T> getOpenRegistry(Class<T> clazz) {
        return (OpenRegistry<T>) registryMap.getOrDefault(clazz, ImmutablePair.ofNull()).getLeft();
    }
    
    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(Class<T> clazz) {
        return (CheckedRegistry<T>) registryMap.getOrDefault(clazz, ImmutablePair.ofNull()).getRight();
    }
    
    public Set<TerraStructure> getStructures() {
        return new HashSet<>(getRegistry(TerraStructure.class).entries());
    }
    
    public ConfigPackTemplate getTemplate() {
        return template;
    }
    
    public Scope getVarScope() {
        return varScope;
    }
    
    public BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder() {
        return biomeProviderBuilder;
    }
    
    public WorldConfig toWorldConfig(TerraWorld world) {
        return new WorldConfig(world, this, main);
    }
    
    public CheckedRegistry<ConfigType<?, ?>> getConfigTypeRegistry() {
        return new CheckedRegistry<ConfigType<?, ?>>(configTypeRegistry) {
            @Override
            @SuppressWarnings("deprecation")
            public void addUnchecked(String identifier, ConfigType<?, ?> value) {
                if(contains(identifier)) throw new UnsupportedOperationException("Cannot override values in ConfigTypeRegistry!");
            }
        };
    }
}
