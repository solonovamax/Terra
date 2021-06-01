package com.dfsek.terra.registry.config;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.factories.BiomeFactory;
import com.dfsek.terra.config.factories.CarverFactory;
import com.dfsek.terra.config.factories.ConfigFactory;
import com.dfsek.terra.config.factories.FloraFactory;
import com.dfsek.terra.config.factories.OreFactory;
import com.dfsek.terra.config.factories.PaletteFactory;
import com.dfsek.terra.config.factories.StructureFactory;
import com.dfsek.terra.config.factories.TreeFactory;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.prototype.ConfigType;
import com.dfsek.terra.config.templates.AbstractableTemplate;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import com.dfsek.terra.config.templates.FloraTemplate;
import com.dfsek.terra.config.templates.OreTemplate;
import com.dfsek.terra.config.templates.PaletteTemplate;
import com.dfsek.terra.config.templates.StructureTemplate;
import com.dfsek.terra.config.templates.TreeTemplate;
import com.dfsek.terra.registry.OpenRegistry;
import com.dfsek.terra.world.population.items.TerraStructure;
import com.dfsek.terra.world.population.items.ores.Ore;

import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;


public class ConfigTypeRegistry extends OpenRegistry<ConfigType<?, ?>> {
    private final BiConsumer<String, ConfigType<?, ?>> callback;
    
    public ConfigTypeRegistry(ConfigPack pack, TerraPlugin main, BiConsumer<String, ConfigType<?, ?>> callback) {
        super(new LinkedHashMap<>()); // Ordered
        this.callback = callback;
        add("PALETTE", new ConfigBuilder<>(new PaletteFactory(), PaletteTemplate::new, Palette.class, () -> new PaletteRegistry(main)));
        add("ORE", new ConfigBuilder<>(new OreFactory(), OreTemplate::new, Ore.class, OreRegistry::new));
        add("FLORA", new ConfigBuilder<>(new FloraFactory(), FloraTemplate::new, Flora.class, () -> new FloraRegistry(main)));
        add("CARVER", new ConfigBuilder<>(new CarverFactory(pack), CarverTemplate::new, UserDefinedCarver.class, CarverRegistry::new));
        add("STRUCTURE", new ConfigBuilder<>(new StructureFactory(), StructureTemplate::new, TerraStructure.class, StructureRegistry::new));
        add("TREE", new ConfigBuilder<>(new TreeFactory(), TreeTemplate::new, Tree.class, TreeRegistry::new));
        add("BIOME",
            new ConfigBuilder<>(new BiomeFactory(pack), () -> new BiomeTemplate(pack, main), BiomeBuilder.class, BiomeRegistry::new));
        add("PACK", new PackBuilder());
    }
    
    @Override
    protected boolean add(String identifier, Entry<ConfigType<?, ?>> value) {
        callback.accept(identifier, value.getValue());
        return super.add(identifier, value);
    }
    
    private static final class PackBuilder implements ConfigType<ConfigTemplate, ConfigPack> {
        
        @Override
        public void callback(ConfigPack pack, TerraPlugin main, ConfigTemplate loadedConfig) {
        
        }
        
        @Override
        public Supplier<OpenRegistry<ConfigPack>> registrySupplier() {
            return OpenRegistry::new;
        }
        
        @Override
        public ConfigTemplate getTemplate(ConfigPack pack, TerraPlugin main) {
            return new ConfigTemplate() {
            };
        }
        
        @Override
        public Class<ConfigPack> getTypeClass() {
            return ConfigPack.class;
        }
    }
    
    
    private static final class ConfigBuilder<T extends AbstractableTemplate, O> implements ConfigType<T, O> {
        private final ConfigFactory<T, O> factory;
        private final Supplier<T> provider;
        private final Class<O> clazz;
        private final Supplier<OpenRegistry<O>> registrySupplier;
        
        private ConfigBuilder(ConfigFactory<T, O> factory, Supplier<T> provider, Class<O> clazz,
                              Supplier<OpenRegistry<O>> registrySupplier) {
            this.factory = factory;
            this.provider = provider;
            this.clazz = clazz;
            this.registrySupplier = registrySupplier;
        }
        
        @SuppressWarnings("deprecation")
        @Override
        public void callback(ConfigPack pack, TerraPlugin main, T loadedConfig) throws LoadException {
            pack.getRegistry(clazz).addUnchecked(loadedConfig.getID(), factory.build(loadedConfig, main));
        }
        
        @Override
        public Supplier<OpenRegistry<O>> registrySupplier() {
            return registrySupplier;
        }
        
        @Override
        public T getTemplate(ConfigPack pack, TerraPlugin main) {
            return provider.get();
        }
        
        @Override
        public Class<O> getTypeClass() {
            return clazz;
        }
    }
}
