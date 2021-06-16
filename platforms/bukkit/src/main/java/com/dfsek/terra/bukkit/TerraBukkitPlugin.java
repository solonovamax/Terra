package com.dfsek.terra.bukkit;

import com.dfsek.tectonic.loading.TypeRegistry;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.addons.annotations.Addon;
import com.dfsek.terra.api.addons.annotations.Author;
import com.dfsek.terra.api.addons.annotations.Version;
import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.TerraCommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.event.TerraEventManager;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.bukkit.command.BukkitCommandAdapter;
import com.dfsek.terra.bukkit.command.FixChunkCommand;
import com.dfsek.terra.bukkit.command.SaveDataCommand;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.handles.BukkitItemHandle;
import com.dfsek.terra.bukkit.handles.BukkitWorldHandle;
import com.dfsek.terra.bukkit.listeners.CommonListener;
import com.dfsek.terra.bukkit.listeners.PaperListener;
import com.dfsek.terra.bukkit.listeners.SpigotListener;
import com.dfsek.terra.bukkit.listeners.TerraListener;
import com.dfsek.terra.bukkit.util.PaperUtil;
import com.dfsek.terra.bukkit.util.VersionUtil;
import com.dfsek.terra.bukkit.world.BukkitBiome;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.config.GenericLoaders;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.profiler.ProfilerImpl;
import com.dfsek.terra.registry.master.AddonRegistry;
import com.dfsek.terra.registry.master.ConfigRegistry;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class TerraBukkitPlugin extends JavaPlugin implements TerraPlugin {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final Map<String, DefaultChunkGenerator3D> generatorMap = new HashMap<>();
    private final Map<World, TerraWorld> worldMap = new HashMap<>();
    private final Map<String, ConfigPack> worlds = new HashMap<>();
    private final Profiler profiler = new ProfilerImpl();
    private final ConfigRegistry registry = new ConfigRegistry();
    private final CheckedRegistry<ConfigPack> checkedRegistry = new CheckedRegistry<>(registry);
    private final PluginConfig config = new PluginConfig();
    private final ItemHandle itemHandle = new BukkitItemHandle();
    private final GenericLoaders genericLoaders = new GenericLoaders(this);
    private final EventManager eventManager = new TerraEventManager(this);
    private final AddonRegistry addonRegistry = new AddonRegistry(new BukkitAddon(this), this);
    private final LockedRegistry<TerraAddon> addonLockedRegistry = new LockedRegistry<>(addonRegistry);
    private WorldHandle handle = new BukkitWorldHandle();
    
    @Override
    public void onDisable() {
        BukkitChunkGeneratorWrapper.saveAll();
    }
    
    @Override
    public void onEnable() {
        // Check for version-y stuff at startup
        boolean shouldInitialize = doVersionCheck();
        if(!shouldInitialize)
            return;
        
        saveDefaultConfig();
        
        Metrics metrics = new Metrics(this, 9017); // Set up bStats. // Magic number go brrr
        metrics.addCustomChart(new Metrics.SingleLineChart("worlds", worldMap::size)); // World number chart.
        
        config.load(this); // Load master config.yml
        LangUtil.load(config.getLanguage(), this); // Load language.
        
        if(config.isDebugProfiler())
            profiler.start();
        
        if(!addonRegistry.loadAll()) {
            logger.error("Failed to load addons. Please correct addon installations to continue.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        registry.loadAll(this); // Load all config packs.
        
        PluginCommand c = Objects.requireNonNull(getCommand("terra"));
        
        CommandManager manager = new TerraCommandManager(this);
        
        
        try {
            CommandUtil.registerAll(manager);
            manager.register("save-data", SaveDataCommand.class);
            manager.register("fix-chunk", FixChunkCommand.class);
        } catch(MalformedCommandException e) { // This should never happen.
            logger.error("Errors occurred while registering commands.\nPlease report this to Terra.", e);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        BukkitCommandAdapter command = new BukkitCommandAdapter(manager);
        
        c.setExecutor(command);
        c.setTabCompleter(command);
        
        
        long save = config.getDataSaveInterval();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, BukkitChunkGeneratorWrapper::saveAll, save,
                                                         save); // Schedule population data saving
        Bukkit.getPluginManager().registerEvents(new CommonListener(this), this); // Register master event listener
        PaperUtil.checkPaper(this);
        
        try {
            Class.forName("io.papermc.paper.event.world.StructureLocateEvent"); // Check if user is on Paper version with event.
            Bukkit.getPluginManager().registerEvents(new PaperListener(this), this); // Register Paper events.
        } catch(ClassNotFoundException e) {
            /*
            The command
            
                fmt -w 72 -g 72 -u text | \
                boxes -a c -p a1h3 -t 4e -d jstone -s82 | \
                sed -Ee 's/\+-+\*\//|------------------------------------------------------------------------------|/g' \
                -e 's/^\s*(.*)$/"\1\\n"/g' -e 's/\///g' -e 's/\*|\+/./g' -e 's/$/ +/g' -e '/^"\| {3}-{72} {3}\|\\n" \+$/d'
            
            was used to create these boxes. Leaving this here in case we want to create more/modify them.
             */
            if(VersionUtil.getSpigotVersionInfo().isPaper()) { // logging with stack trace to be annoying.
                logger.warn("\n" +
                            ".------------------------------------------------------------------------------.\n" +
                            "|                                                                              |\n" +
                            "|      You are using an outdated version of Paper. This version does not       |\n" +
                            "|       contain StructureLocateEvent. Terra will now fall back to Spigot       |\n" +
                            "|       events. This will prevent cartographer villagers from spawning,        |\n" +
                            "|       and cause structure location to not function. If you want these        |\n" +
                            "|      functionalities, update to the latest build of Paper. If you use a      |\n" +
                            "|      fork, update to the latest version, then if you still receive this      |\n" +
                            "|             message, ask the fork developer to update upstream.              |\n" +
                            "|                                                                              |\n" +
                            "|------------------------------------------------------------------------------|", e);
            } else {
                logger.warn("\n" +
                            ".------------------------------------------------------------------------------.\n" +
                            "|                                                                              |\n" +
                            "|    Paper is not in use. Falling back to Spigot events. This will prevent     |\n" +
                            "|    cartographer villagers from spawning, and cause structure location to     |\n" +
                            "|      not function. If you want these functionalities (and all the other      |\n" +
                            "|     benefits that Paper offers), upgrade your server to Paper. Find out      |\n" +
                            "|                         more at https://papermc.io/                          |\n" +
                            "|                                                                              |\n" +
                            "|------------------------------------------------------------------------------|", e);
            }
            
            Bukkit.getPluginManager().registerEvents(new SpigotListener(this), this); // Register Spigot event listener
        }
    }
    
    @Override
    public @Nullable
    ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new BukkitChunkGeneratorWrapper(generatorMap.computeIfAbsent(worldName, name -> {
            if(!registry.contains(id))
                throw new IllegalArgumentException("No such config pack \"" + id + "\"");
            ConfigPack pack = registry.get(id);
            worlds.put(worldName, pack);
            return new DefaultChunkGenerator3D(registry.get(id), this);
        }));
    }
    
    @Override
    public void register(TypeRegistry registry) {
        registry.registerLoader(BlockData.class, (t, o, l) -> handle.createBlockData((String) o))
                .registerLoader(Biome.class, (t, o, l) -> new BukkitBiome(org.bukkit.block.Biome.valueOf((String) o)))
                .registerLoader(EntityType.class, (t, o, l) -> EntityType.valueOf((String) o));
        genericLoaders.register(registry);
    }
    
    private boolean doVersionCheck() {
        logger.info("Running on version {} with {}.", VersionUtil.getMinecraftVersionInfo(), VersionUtil.getSpigotVersionInfo());
        
        if(VersionUtil.getMinecraftVersionInfo().getMinor() <= 15)
            logger.warn("Terra is running on an outdated Bukkit version. Some functionality may be limited or not work as intended");
        
        if(!VersionUtil.getSpigotVersionInfo().isSpigot())
            logger.error("YOU ARE RUNNING A CRAFTBUKKIT OR BUKKIT SERVER JAR. PLEASE UPGRADE TO PAPER SPIGOT.");
        
        if(VersionUtil.getSpigotVersionInfo().isYaptopia())
            logger.warn("Yaptopia is a highly unstable fork of spigot. You may experience various issues with it.");
        
        if(!VersionUtil.getSpigotVersionInfo().isMohist()) {
            if(System.getProperty("IKnowMohistCausesLotsOfIssuesButIWillUseItAnyways") == null) {
                //noinspection CodeBlock2Expr
                Runnable runnable = () -> {
                    logger.error("\n" +
                                 ".----------------------------------------------------------------------------------.\n" +
                                 "|                                                                                  |\n" +
                                 "|                                ⚠ !! Warning !! ⚠                               |\n" +
                                 "|                                                                                  |\n" +
                                 "|                         You are currently using Mohist.                          |\n" +
                                 "|                                                                                  |\n" +
                                 "|                                Do not use Mohist.                                |\n" +
                                 "|                                                                                  |\n" +
                                 "|   The concept of combining the rigid Bukkit platform, which assumes a 100%       |\n" +
                                 "|   Vanilla server, with the flexible Forge platform, which allows changing        |\n" +
                                 "|   core components of the game, simply does not work. These platforms are         |\n" +
                                 "|   incompatible at a conceptual level, the only way to combine them would         |\n" +
                                 "|   be to make incompatible changes to both. As a result, Mohist's Bukkit          |\n" +
                                 "|   API implementation is not compliant. This will cause many plugins to           |\n" +
                                 "|   break. Rather than fix their platform, Mohist has chosen to distribute         |\n" +
                                 "|   unofficial builds of plugins they deem to be \"fixed\". These builds are not     |\n" +
                                 "|   \"fixed\", they are simply hacked together to work with Mohist's half-baked      |\n" +
                                 "|   Bukkit implementation. To distribute these as \"fixed\" versions implies that:   |\n" +
                                 "|       - These builds are endorsed by the original developers. (They are not)     |\n" +
                                 "|       - The issue is on the plugin's end, not Mohist's. (It is not. The issue    |\n" +
                                 "|       is that Mohist chooses to not create a compliant Bukkit implementation)    |\n" +
                                 "|   Please, do not use Mohist. It causes issues with most plugins, and rather      |\n" +
                                 "|   than fixing their platform, Mohist has chosen to distribute unofficial         |\n" +
                                 "|   hacked-together builds of plugins, calling them \"fixed\". If you want           |\n" +
                                 "|   to use a server API with Forge mods, look at the Sponge project, an            |\n" +
                                 "|   API that is designed to be implementation-agnostic, with first-party           |\n" +
                                 "|   support for the Forge mod loader. You are bound to encounter issues if         |\n" +
                                 "|   you use Terra with Mohist. We will provide NO SUPPORT for servers running      |\n" +
                                 "|   Mohist. If you wish to proceed anyways, you can add the JVM System Property    |\n" +
                                 "|   \"IKnowMohistCausesLotsOfIssuesButIWillUseItAnyways\" to enable the plugin. No   |\n" +
                                 "|   support will be provided for servers running Mohist.                           |\n" +
                                 "|                                                                                  |\n" +
                                 "|                   Because of this **TERRA HAS BEEN DISABLED**.                   |\n" +
                                 "|                    Do not come ask us why it is not working.                     |\n" +
                                 "|                                                                                  |\n" +
                                 "|----------------------------------------------------------------------------------|");
                };
                
                runnable.run();
                //noinspection deprecation
                Bukkit.getScheduler().scheduleAsyncDelayedTask(this, runnable, 200L);
                // Bukkit.shutdown(); // we're not *that* evil
                setEnabled(false);
                return false;
            } else {
                logger.warn("You are using Mohist, so we will not give you any support for issues that may arise. " +
                            "Since you enabled the \"IKnowMohistCausesLotsOfIssuesButIWillUseItAnyways\" flag, we won't disable Terra. " +
                            "But be warned.");
                logger.warn("I felt a great disturbance in the JVM, as if millions of plugins suddenly cried out in stack traces and" +
                            " were suddenly silenced. I fear something terrible has happened.\n- Astrash");
            }
        }
        return true;
    }
    
    public void setHandle(WorldHandle handle) {
        logger.warn("\n" +
                    ".------------------------------------------------------------------------------.\n" +
                    "|                                                                              |\n" +
                    "|   A third-party addon has injected a custom WorldHandle! If you encounter    |\n" +
                    "|      issues, try *without* the addon before reporting to Terra. Report       |\n" +
                    "|              issues with the addon to the addon's maintainers!               |\n" +
                    "|                                                                              |\n" +
                    "|------------------------------------------------------------------------------|");
        this.handle = handle;
    }
    
    @Override
    public WorldHandle getWorldHandle() {
        return handle;
    }
    
    public boolean reload() {
        config.load(this);
        LangUtil.load(config.getLanguage(), this); // Load language.
        boolean succeed = registry.loadAll(this);
        Map<World, TerraWorld> newMap = new HashMap<>();
        worldMap.forEach((world, tw) -> {
            tw.getConfig().getSamplerCache().clear();
            String packID = tw.getConfig().getTemplate().getID();
            newMap.put(world, new TerraWorld(world, registry.get(packID), this));
        });
        worldMap.clear();
        worldMap.putAll(newMap);
        return succeed;
    }
    
    @Override
    public String platformName() {
        return "Bukkit";
    }
    
    @Override
    public EventManager getEventManager() {
        return eventManager;
    }
    
    @Override
    public void runPossiblyUnsafeTask(Runnable task) {
        Bukkit.getScheduler().runTask(this, task);
    }
    
    @Override
    public LockedRegistry<TerraAddon> getAddons() {
        return addonLockedRegistry;
    }
    
    public CheckedRegistry<ConfigPack> getConfigRegistry() {
        return checkedRegistry;
    }
    
    @Override
    public ItemHandle getItemHandle() {
        return itemHandle;
    }
    
    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }
    
    @Override
    public Profiler getProfiler() {
        return profiler;
    }
    
    @NotNull
    @Override
    public PluginConfig getTerraConfig() {
        return config;
    }
    
    public TerraWorld getWorld(World world) {
        BukkitWorld w = (BukkitWorld) world;
        if(!w.isTerraWorld())
            throw new IllegalArgumentException("Not a Terra world! " + w.getGenerator());
        if(!worlds.containsKey(w.getName())) {
            logger.warn("Unexpected world load detected: \"{}\"", w.getName());
            return new TerraWorld(w, ((TerraChunkGenerator) w.getGenerator().getHandle()).getConfigPack(), this);
        }
        return worldMap.computeIfAbsent(w, w2 -> new TerraWorld(w, worlds.get(w.getName()), this));
    }
    
    
    @Addon("Terra-Bukkit")
    @Version("1.0.0")
    @Author("Terra")
    private static final class BukkitAddon extends TerraAddon {
        private final TerraPlugin main;
        
        private BukkitAddon(TerraPlugin main) {
            this.main = main;
        }
        
        @Override
        public void initialize() {
            main.getEventManager().registerListener(this, new TerraListener(main));
        }
    }
}
