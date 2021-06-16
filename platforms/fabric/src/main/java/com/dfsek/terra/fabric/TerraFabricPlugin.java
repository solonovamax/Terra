package com.dfsek.terra.fabric;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Author;
import com.dfsek.terra.api.addons.annotations.Version;
import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.TerraCommandManager;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.TerraEventManager;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.annotations.Priority;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.transform.NotNullValidator;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.builder.BiomeBuilder;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.fabric.generation.FabricChunkGeneratorWrapper;
import com.dfsek.terra.fabric.generation.PopulatorFeature;
import com.dfsek.terra.fabric.generation.TerraBiomeSource;
import com.dfsek.terra.fabric.handle.FabricItemHandle;
import com.dfsek.terra.fabric.handle.FabricWorldHandle;
import com.dfsek.terra.fabric.mixin.access.BiomeEffectsAccessor;
import com.dfsek.terra.fabric.mixin.access.GeneratorTypeAccessor;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.exception.DuplicateEntryException;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class TerraFabricPlugin implements TerraPlugin, ModInitializer {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final PopulatorFeature POPULATOR_FEATURE = new PopulatorFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredFeature<?, ?> POPULATOR_CONFIGURED_FEATURE = POPULATOR_FEATURE.configure(FeatureConfig.DEFAULT)
                                                                                                .decorate(Decorator.NOPE.configure(
                                                                                                        NopeDecoratorConfig.INSTANCE));
    private static TerraFabricPlugin instance;
    private final Map<Long, TerraWorld> worldMap = new HashMap<>();
    private final EventManager eventManager = new TerraEventManager(this);
    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    
    private final Profiler profiler = new ProfilerImpl();
    
    private final ItemHandle itemHandle = new FabricItemHandle();
    private final WorldHandle worldHandle = new FabricWorldHandle();
    private final ConfigRegistry registry = new ConfigRegistry();
    private final CheckedRegistry<ConfigPack> checkedRegistry = new CheckedRegistry<>(registry);
    private final AddonRegistry addonRegistry = new AddonRegistry(new FabricAddon(this), this);
    private final LockedRegistry<TerraAddon> addonLockedRegistry = new LockedRegistry<>(addonRegistry);
    private final PluginConfig config = new PluginConfig();
    private final Transformer<String, Biome> biomeFixer = new Transformer.Builder<String, Biome>()
            .addTransform(id -> BuiltinRegistries.BIOME.get(Identifier.tryParse(id)), new NotNullValidator<>())
            .addTransform(id -> BuiltinRegistries.BIOME.get(Identifier.tryParse("minecraft:" + id.toLowerCase())), new NotNullValidator<>())
            .build();
    private File dataFolder;
    
    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getTemplate().getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }
    
    public static TerraFabricPlugin getInstance() {
        return instance;
    }
    
    @Override
    public void register(TypeRegistry registry) {
        genericLoaders.register(registry);
        registry
                .registerLoader(BlockData.class, (t, o, l) -> worldHandle.createBlockData((String) o))
                .registerLoader(com.dfsek.terra.api.platform.world.Biome.class, (t, o, l) -> biomeFixer.translate((String) o));
    }
    
    public void packInit() {
        logger.info("Loading config packs...");
        registry.loadAll(this);
        
        registry.forEach(pack -> pack.getRegistry(BiomeBuilder.class)
                                     .forEach((id, biome) -> Registry.register(BuiltinRegistries.BIOME,
                                                                               new Identifier("terra", createBiomeID(pack, id)),
                                                                               createBiome(biome)))); // Register all Terra biomes.
        
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            registry.forEach(pack -> {
                final GeneratorType generatorType = new GeneratorType("terra." + pack.getTemplate().getID()) {
                    @Override
                    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry,
                                                               Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
                        return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
                    }
                };
                //noinspection ConstantConditions
                ((GeneratorTypeAccessor) generatorType).setTranslationKey(new LiteralText("Terra:" + pack.getTemplate().getID()));
                GeneratorTypeAccessor.getValues().add(generatorType);
            });
        }
        
        logger.info("Loaded packs.");
    }
    
    @Override
    public void onInitialize() {
        instance = this;
        
        this.dataFolder = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Terra");
        saveDefaultConfig();
        config.load(this);
        LangUtil.load(config.getLanguage(), this);
        logger.info("Initializing Terra...");
        
        if(config.isDebugProfiler())
            profiler.start();
        
        if(!addonRegistry.loadAll()) {
            throw new IllegalStateException("Failed to load addons. Please correct addon installations to continue.");
        }
        logger.info("Loaded addons.");
        
        registry.loadAll(this);
        
        logger.info("Loaded packs.");
        
        Registry.register(Registry.FEATURE, new Identifier("terra", "flora_populator"), POPULATOR_FEATURE);
        RegistryKey<ConfiguredFeature<?, ?>> floraKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
                                                                       new Identifier("terra", "flora_populator"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, floraKey.getValue(), POPULATOR_CONFIGURED_FEATURE);
        
        registry.forEach(pack -> pack.getRegistry(BiomeBuilder.class)
                                     .forEach((id, biome) -> Registry.register(BuiltinRegistries.BIOME,
                                                                               new Identifier("terra", createBiomeID(pack, id)),
                                                                               createBiome(biome)))); // Register all Terra biomes.
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier("terra:terra"), FabricChunkGeneratorWrapper.CODEC);
        Registry.register(Registry.BIOME_SOURCE, new Identifier("terra:terra"), TerraBiomeSource.CODEC);
        
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            registry.forEach(pack -> {
                final GeneratorType generatorType = new GeneratorType("terra." + pack.getTemplate().getID()) {
                    @Override
                    protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry,
                                                               Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
                        return new FabricChunkGeneratorWrapper(new TerraBiomeSource(biomeRegistry, seed, pack), seed, pack);
                    }
                };
                //noinspection ConstantConditions
                ((GeneratorTypeAccessor) generatorType).setTranslationKey(new LiteralText("Terra:" + pack.getTemplate().getID()));
                GeneratorTypeAccessor.getValues().add(generatorType);
            });
        }
        
        CommandManager manager = new TerraCommandManager(this);
        try {
            CommandUtil.registerAll(manager);
        } catch(MalformedCommandException e) {
            e.printStackTrace(); // TODO do something here even though this should literally never happen
        }
        
        
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
                                                       int max = manager.getMaxArgumentDepth();
                                                       RequiredArgumentBuilder<ServerCommandSource, String> arg =
                                                               argument("arg" + (max - 1), StringArgumentType.word());
                                                       for(int i = 0; i < max; i++) {
                                                           RequiredArgumentBuilder<ServerCommandSource, String> next =
                                                                   argument("arg" + (max - i - 1), StringArgumentType.word());
                
                                                           arg = next.then(assemble(arg, manager));
                                                       }
            
                                                       dispatcher.register(literal("terra").executes(context -> 1)
                                                                                           .then(assemble(arg, manager)));
                                                       dispatcher.register(literal("te").executes(context -> 1)
                                                                                        .then(assemble(arg, manager)));
                                                       //dispatcher.register(literal("te").redirect(root));
                                                   }
                                                  );
        logger.info("Finished initialization.");
    }
    
    private Biome createBiome(BiomeBuilder biome) {
        BiomeTemplate template = biome.getTemplate();
        Map<String, Integer> colors = template.getColors();
        
        Biome vanilla = (Biome) (new ArrayList<>(biome.getVanillaBiomes().getContents()).get(0)).getHandle();
        
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        generationSettings.surfaceBuilder(SurfaceBuilder.DEFAULT.withConfig(
                new TernarySurfaceConfig(Blocks.GRASS_BLOCK.getDefaultState(),
                                         Blocks.DIRT.getDefaultState(),
                                         Blocks.GRAVEL.getDefaultState())
                                                                           )); // It needs a surfacebuilder, even though we dont use it.
        generationSettings.feature(GenerationStep.Feature.VEGETAL_DECORATION, POPULATOR_CONFIGURED_FEATURE);
        
        
        BiomeEffectsAccessor accessor = (BiomeEffectsAccessor) vanilla.getEffects();
        BiomeEffects.Builder effects =
                new BiomeEffects.Builder().waterColor(colors.getOrDefault("water", accessor.getWaterColor()))
                                          .waterFogColor(colors.getOrDefault("water-fog", accessor.getWaterFogColor()))
                                          .fogColor(colors.getOrDefault("fog", accessor.getFogColor()))
                                          .skyColor(colors.getOrDefault("sky", accessor.getSkyColor()))
                                          .grassColorModifier(accessor.getGrassColorModifier());
        
        if(colors.containsKey("grass"))
            effects.grassColor(colors.get("grass"));
        else
            accessor.getGrassColor().ifPresent(effects::grassColor);
        if(colors.containsKey("foliage"))
            effects.foliageColor(colors.get("foliage"));
        else
            accessor.getFoliageColor().ifPresent(effects::foliageColor);
        
        return new Biome.Builder().precipitation(vanilla.getPrecipitation())
                                  .category(vanilla.getCategory())
                                  .depth(vanilla.getDepth())
                                  .scale(vanilla.getScale())
                                  .temperature(vanilla.getTemperature())
                                  .downfall(vanilla.getDownfall())
                                  .effects(effects.build())
                                  .spawnSettings(vanilla.getSpawnSettings())
                                  .generationSettings(generationSettings.build())
                                  .build();
    }
    
    private RequiredArgumentBuilder<ServerCommandSource, String> assemble(RequiredArgumentBuilder<ServerCommandSource, String> in,
                                                                          CommandManager manager) {
        return in.suggests((context, builder) -> {
            List<String> args = parseCommand(context.getInput());
            CommandSender sender = (CommandSender) context.getSource();
            try {
                sender = (Entity) context.getSource().getEntityOrThrow();
            } catch(CommandSyntaxException ignore) {
            }
            try {
                manager.tabComplete(args.remove(0), sender, args).forEach(builder::suggest);
            } catch(CommandException e) {
                sender.sendMessage(e.getMessage());
            }
            return builder.buildFuture();
        }).executes(context -> {
            List<String> args = parseCommand(context.getInput());
            CommandSender sender = (CommandSender) context.getSource();
            try {
                sender = (Entity) context.getSource().getEntityOrThrow();
            } catch(CommandSyntaxException ignore) {
            }
            try {
                manager.execute(args.remove(0), sender, args);
            } catch(CommandException e) {
                context.getSource().sendError(new LiteralText(e.getMessage()));
            }
            return 1;
        });
    }
    
    private List<String> parseCommand(String command) {
        if(command.startsWith("/terra ")) command = command.substring("/terra ".length());
        else if(command.startsWith("/te ")) command = command.substring("/te ".length());
        List<String> c = new ArrayList<>(Arrays.asList(command.split(" ")));
        if(command.endsWith(" ")) c.add("");
        return c;
    }
    
    public TerraWorld getWorld(long seed) {
        TerraWorld world = worldMap.get(seed);
        if(world == null) throw new IllegalArgumentException("No world exists with seed " + seed);
        return world;
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return worldHandle;
    }
    
    @Override
    public TerraWorld getWorld(World world) {
        return worldMap.computeIfAbsent(world.getSeed(), w -> {
            logger.info("Loading world " + w);
            return new TerraWorld(world, ((FabricChunkGeneratorWrapper) world.getGenerator()).getPack(), this);
        });
    }
    
    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }
    
    @Override
    public File getDataFolder() {
        return dataFolder;
    }
    
    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }
    
    @Override
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return checkedRegistry;
    }
    
    @Override
    public LockedRegistry<TerraAddon> getAddons() {
        return addonLockedRegistry;
    }
    
    @Override
    public boolean reload() {
        config.load(this);
        LangUtil.load(config.getLanguage(), this); // Load language.
        boolean succeed = registry.loadAll(this);
        Map<Long, TerraWorld> newMap = new HashMap<>();
        worldMap.forEach((seed, tw) -> {
            tw.getConfig().getSamplerCache().clear();
            String packID = tw.getConfig().getTemplate().getID();
            newMap.put(seed, new TerraWorld(tw.getWorld(), registry.get(packID), this));
        });
        worldMap.clear();
        worldMap.putAll(newMap);
        return succeed;
    }
    
    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public void saveDefaultConfig() {
        try(InputStream stream = getClass().getResourceAsStream("/config.yml")) {
            File configFile = new File(getDataFolder(), "config.yml");
            if(!configFile.exists()) FileUtils.copyInputStreamToFile(stream, configFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String platformName() {
        return "Fabric";
    }
    
    @Override
    public EventManager getEventManager() {
        return eventManager;
    }
    
    @Override
    public Profiler getProfiler() {
        return profiler;
    }
    
    
    @Addon("Terra-Fabric")
    @Author("Terra")
    @Version("1.0.0")
    private static final class FabricAddon extends TerraAddon implements EventListener {
        
        private final TerraPlugin main;
        
        private FabricAddon(TerraPlugin main) {
            this.main = main;
        }
        
        @Override
        public void initialize() {
            main.getEventManager().registerListener(this, this);
        }
        
        @Priority(Priority.LOWEST)
        @Global
        public void injectTrees(ConfigPackPreLoadEvent event) {
            CheckedRegistry<Tree> treeRegistry = event.getPack().getRegistry(Tree.class);
            injectTree(treeRegistry, "BROWN_MUSHROOM", ConfiguredFeatures.HUGE_BROWN_MUSHROOM);
            injectTree(treeRegistry, "RED_MUSHROOM", ConfiguredFeatures.HUGE_RED_MUSHROOM);
            injectTree(treeRegistry, "JUNGLE", ConfiguredFeatures.MEGA_JUNGLE_TREE);
            injectTree(treeRegistry, "JUNGLE_COCOA", ConfiguredFeatures.JUNGLE_TREE);
            injectTree(treeRegistry, "LARGE_OAK", ConfiguredFeatures.FANCY_OAK);
            injectTree(treeRegistry, "LARGE_SPRUCE", ConfiguredFeatures.PINE);
            injectTree(treeRegistry, "SMALL_JUNGLE", ConfiguredFeatures.JUNGLE_TREE);
            injectTree(treeRegistry, "SWAMP_OAK", ConfiguredFeatures.SWAMP_TREE);
            injectTree(treeRegistry, "TALL_BIRCH", ConfiguredFeatures.BIRCH_TALL);
            injectTree(treeRegistry, "ACACIA", ConfiguredFeatures.ACACIA);
            injectTree(treeRegistry, "BIRCH", ConfiguredFeatures.BIRCH);
            injectTree(treeRegistry, "DARK_OAK", ConfiguredFeatures.DARK_OAK);
            injectTree(treeRegistry, "OAK", ConfiguredFeatures.OAK);
            injectTree(treeRegistry, "CHORUS_PLANT", ConfiguredFeatures.CHORUS_PLANT);
            injectTree(treeRegistry, "SPRUCE", ConfiguredFeatures.SPRUCE);
            injectTree(treeRegistry, "JUNGLE_BUSH", ConfiguredFeatures.JUNGLE_BUSH);
            injectTree(treeRegistry, "MEGA_SPRUCE", ConfiguredFeatures.MEGA_SPRUCE);
            injectTree(treeRegistry, "CRIMSON_FUNGUS", ConfiguredFeatures.CRIMSON_FUNGI);
            injectTree(treeRegistry, "WARPED_FUNGUS", ConfiguredFeatures.WARPED_FUNGI);
        }
        
        
        private void injectTree(CheckedRegistry<Tree> registry, String id, ConfiguredFeature<?, ?> tree) {
            try {
                registry.add(id, (Tree) tree);
            } catch(DuplicateEntryException ignore) {
            }
        }
    }
}
