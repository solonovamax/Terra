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
import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.generic.pair.ImmutablePair;
import com.dfsek.terra.api.util.seeded.NoiseProvider;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.dummy.DummyWorld;
import com.dfsek.terra.config.load.PackEntry;
import com.dfsek.terra.config.load.PackFileLoader;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Represents a Terra configuration pack.
 */
@SuppressWarnings("WeakerAccess")
public final class ConfigPack implements LoaderRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final TerraPlugin main;
    private final PackFileLoader loader;
    private final AbstractConfigLoader abstractConfigLoader = new AbstractConfigLoader();
    private final ConfigLoader selfLoader = new ConfigLoader();
    private final Scope varScope = new Scope();
    private final BiomeProvider.BiomeProviderBuilder biomeProviderBuilder;
    
    private final ConfigPackTemplate template = new ConfigPackTemplate();
    
    
    private final ConfigTypeRegistry configTypeRegistry;
    private final Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> registryMap = newRegistryMap();
    
    public ConfigPack(final TerraPlugin main, final File configPackFile) throws ConfigException, IOException {
        this.main = main;
        
        final long startTime = System.nanoTime();
        
        configTypeRegistry = new ConfigTypeRegistry(this, main, (id, configType) -> {
            final OpenRegistry<?> openRegistry = configType.registrySupplier().get();
            registryMap.put(configType.getTypeClass(), ImmutablePair.of(openRegistry, new CheckedRegistry<>(openRegistry)));
        });
        
        loader = new PackFileLoader(configPackFile);
        
        register(selfLoader);
        main.register(selfLoader);
        register(abstractConfigLoader);
        main.register(abstractConfigLoader);
        
        final PackEntry entry = loader.getSingleEntry("pack.yml");
        final Configuration packYmlConfiguration;
        
        try(final InputStream packYmlStream = entry.getEntryInputStream()) {
            packYmlConfiguration = new Configuration(packYmlStream);
        }
        
        selfLoader.load(template, packYmlConfiguration);
        
        logger.info("pack.yml is valid for config pack '{}'. Attempting to load all entities.", template.getID());
        
        load(startTime);
        
        final ConfigPackPostTemplate packPostTemplate = new ConfigPackPostTemplate(); // Dummy provider to catch load time errors
        selfLoader.load(packPostTemplate, packYmlConfiguration);
        biomeProviderBuilder = packPostTemplate.getProviderBuilder();
        
        biomeProviderBuilder.build(0); // Build dummy provider to catch errors at load time.
        
        toWorldConfig(new TerraWorld(new DummyWorld(), this, main)); // Build now to catch any errors immediately.
        
        checkDeadEntries();
    }
    
    private static <R> void putPair(final Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> map, final Class<R> key,
                                    final OpenRegistry<R> registry) {
        map.put(key, ImmutablePair.of(registry, new CheckedRegistry<>(registry)));
    }
    
    @Override
    public void register(final TypeRegistry registry) {
        registry.registerLoader(ConfigType.class, configTypeRegistry)
                .registerLoader(BufferedImage.class, new BufferedImageLoader(loader))
                .registerLoader(SingleBiomeProviderTemplate.class, SingleBiomeProviderTemplate::new)
                .registerLoader(BiomePipelineTemplate.class, () -> new BiomePipelineTemplate(main))
                .registerLoader(ImageProviderTemplate.class, () -> new ImageProviderTemplate(getRegistry(BiomeBuilder.class)))
                .registerLoader(ImageSamplerTemplate.class, () -> new ImageProviderTemplate(getRegistry(BiomeBuilder.class)))
                .registerLoader(NoiseSeeded.class, new NoiseSamplerBuilderLoader(getOpenRegistry(NoiseProvider.class)));
    }
    
    public WorldConfig toWorldConfig(final TerraWorld world) {
        return new WorldConfig(world, this, main);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> newRegistryMap() {
        final Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> map =
                new HashMap<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>>() {
                    @Override
                    public ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>> put(final Class<?> key,
                                                                                  final ImmutablePair<OpenRegistry<?>,
                                                                                          CheckedRegistry<?>> value) {
                        selfLoader.registerLoader(key, value.getLeft());
                        abstractConfigLoader.registerLoader(key, value.getLeft());
                        return super.put(key, value);
                    }
                }; // TODO: 2021-06-21 I don't like this
        
        putPair(map, NoiseProvider.class, new NoiseRegistry());
        putPair(map, FunctionBuilder.class, (OpenRegistry<FunctionBuilder>) (Object) new FunctionRegistry());
        putPair(map, LootTable.class, new LootRegistry());
        putPair(map, StructureScript.class, new ScriptRegistry());
        
        return map;
    }
    
    private void checkDeadEntries() {
        registryMap.forEach((clazz, pair) -> pair.getLeft()
                                                 .getDeadEntries()
                                                 .forEach((id, value) -> logger.warn("Dead entry in '{}' registry: '{}'", clazz, id)));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void load(final long startTime) throws ConfigException, IOException {
        main.getEventManager().callEvent(new ConfigPackPreLoadEvent(this));
        
        for(final Map.Entry<String, Double> internalVariable : template.getVariables().entrySet()) {
            varScope.create(internalVariable.getKey(), internalVariable.getValue());
        }
        
        loader.addEntryProcessor("tesf", entry -> {
            try {
                final Registry<LootTable> lootRegistry = getRegistry(LootTable.class);
                final Registry<StructureScript> scriptRegistry = getRegistry(StructureScript.class);
                final Registry<FunctionBuilder> functionRegistry = getRegistry(FunctionBuilder.class);
                
                final StructureScript structureScript = new StructureScript(entry.getEntryInputStream(),
                                                                            main, scriptRegistry, lootRegistry, functionRegistry);
                getOpenRegistry(StructureScript.class).add(structureScript.getId(), structureScript);
            } catch(final ParseException e) {
                throw new LoadException(String.format("Unable to load script \"%s\"", entry.getName()), e);
            }
        });
        
        loader.addEntryProcessor("structures/loot", "json", entry -> {
            try {
                getOpenRegistry(LootTable.class).add(entry.getName(), new LootTable(entry.getEntryInputStream(), main));
            } catch(final IOException | NullPointerException | org.json.simple.parser.ParseException e) {
                throw new LoadException(String.format("Unable to load loot table \"%s\"", entry.getName()), e);
            }
        });
        
        final Collection<Configuration> configurations = new ArrayList<>();
        
        loader.addEntryProcessor("yml", entry -> configurations.add(new Configuration(entry.getEntryInputStream(), entry.getName())));
        
        final int entriesLoaded = loader.processEntries();
        
        final Map<ConfigType<? extends ConfigTemplate, ?>, List<Configuration>> configs = new HashMap<>();
        
        for(final Configuration configuration : configurations) {
            final ProtoConfig config = new ProtoConfig();
            selfLoader.load(config, configuration);
            configs.computeIfAbsent(config.getType(), configType -> new ArrayList<>()).add(configuration);
        }
        
        for(final ConfigType<?, ?> configType : configTypeRegistry.entries()) {
            for(final ConfigTemplate config : abstractConfigLoader.loadConfigs(
                    configs.getOrDefault(configType, Collections.emptyList()),
                    () -> configType.getTemplate(this, main))) {
                ((ConfigType) configType).callback(this, main, config);
            }
        }
        
        logger.info("Loaded {} config files for pack {}.", entriesLoaded, template.getID());
        
        main.getEventManager().callEvent(new ConfigPackPostLoadEvent(this));
        
        logger.info("Loaded config pack '{}' v{} by {} in {}ms.", template.getID(), template.getVersion(), template.getAuthor(),
                    TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
    }
    
    public BiomeProvider.BiomeProviderBuilder getBiomeProviderBuilder() {
        return biomeProviderBuilder;
    }
    
    
    public CheckedRegistry<ConfigType<?, ?>> getConfigTypeRegistry() {
        return new ConfigTypeCheckedRegistry(configTypeRegistry);
    }
    
    @SuppressWarnings("unchecked")
    <T> OpenRegistry<T> getOpenRegistry(final Class<T> clazz) {
        return (OpenRegistry<T>) registryMap.getOrDefault(clazz, ImmutablePair.ofNull()).getLeft();
    }
    
    @SuppressWarnings("unchecked")
    public <T> CheckedRegistry<T> getRegistry(final Class<T> clazz) {
        return (CheckedRegistry<T>) registryMap.getOrDefault(clazz, ImmutablePair.ofNull()).getRight();
    }
    
    Map<Class<?>, ImmutablePair<OpenRegistry<?>, CheckedRegistry<?>>> getRegistryMap() {
        return Collections.unmodifiableMap(registryMap);
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
    
    private static class ConfigTypeCheckedRegistry extends CheckedRegistry<ConfigType<?, ?>> {
        ConfigTypeCheckedRegistry(final ConfigTypeRegistry configTypeRegistry) {
            super(configTypeRegistry);
        }
        
        @Override
        @SuppressWarnings("deprecation")
        public void addUnchecked(final String identifier, final ConfigType<?, ?> value) {
            if(contains(identifier))
                throw new UnsupportedOperationException("Cannot override values in ConfigTypeRegistry!");
        }
    }
}
