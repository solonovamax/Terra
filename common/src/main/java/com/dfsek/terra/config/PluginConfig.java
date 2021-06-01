package com.dfsek.terra.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.util.JarUtil;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.jar.JarFile;


@SuppressWarnings("FieldMayBeFinal")
public class PluginConfig implements ConfigTemplate {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Value("debug.commands")
    @Default
    private boolean debugCommands = false;
    
    @Value("debug.log")
    @Default
    private boolean debugLog = false;
    
    @Value("debug.profiler")
    @Default
    private boolean debugProfiler = false;
    
    @Value("debug.script")
    @Default
    private boolean debugScript = false;
    
    @Value("language")
    @Default
    private String language = "en_us";
    
    @Value("data-save")
    @Default
    private Duration dataSave = Duration.parse("PT6M");
    
    @Value("biome-search-resolution")
    @Default
    private int biomeSearch = 4;
    
    @Value("cache.carver")
    @Default
    private int carverCache = 256;
    
    @Value("cache.structure")
    @Default
    private int structureCache = 32;
    
    @Value("cache.sampler")
    @Default
    private int samplerCache = 64;
    
    @Value("cache.biome")
    @Default
    private int biomeCache = 512;
    
    @Value("cache.biome-provider")
    @Default
    private int providerCache = 32;
    
    @Value("dump-default")
    @Default
    private boolean dumpDefaultConfig = true;
    
    @Value("script.max-recursion")
    @Default
    private int maxRecursion = 1000;
    
    public void load(TerraPlugin main) {
        logger.info("Loading config values");
        try(FileInputStream file = new FileInputStream(new File(main.getDataFolder(), "config.yml"))) {
            ConfigLoader loader = new ConfigLoader();
            loader.load(this, file);
            if(dumpDefaultConfig) { // Don't dump default config if already loaded.
                try(JarFile jar = main.getModJar()) {
                    JarUtil.copyResourcesToDirectory(jar, "packs", new File(main.getDataFolder(), "packs").toString());
                } catch(IOException | URISyntaxException e) {
                    logger.error("Failed to dump default config files!\n" +
                                 "Either you're on Forge, or this is a bug. If it's the latter, report this to Terra!", e);
                }
            }
        } catch(ConfigException | IOException e) {
            logger.error("Error while loading config!", e);
        }
        
        if(isDebugCommands()) logger.info("Debug commands enabled.");
        if(isDebugLogging()) logger.info("Debug logging enabled.");
        if(isDebugProfiler()) logger.info("Debug profiler enabled.");
        if(isDebugScript()) logger.info("Script debug blocks enabled.");
    }
    
    public int getBiomeCache() {
        return biomeCache;
    }
    
    public int getBiomeSearchResolution() {
        return biomeSearch;
    }
    
    public int getCarverCacheSize() {
        return carverCache;
    }
    
    public long getDataSaveInterval() {
        return dataSave.toMillis() / 20L;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public int getMaxRecursion() {
        return maxRecursion;
    }
    
    public int getProviderCache() {
        return providerCache;
    }
    
    public int getSamplerCache() {
        return samplerCache;
    }
    
    public int getStructureCache() {
        return structureCache;
    }
    
    public boolean isDebugCommands() {
        return debugCommands;
    }
    
    public boolean isDebugLogging() {
        return debugLog;
    }
    
    public boolean isDebugProfiler() {
        return debugProfiler;
    }
    
    public boolean isDebugScript() {
        return debugScript;
    }
}
