package com.dfsek.terra.api;

import com.dfsek.terra.api.addons.TerraAddon;
import com.dfsek.terra.api.event.EventManager;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.registry.CheckedRegistry;
import com.dfsek.terra.api.registry.LockedRegistry;
import com.dfsek.terra.api.util.JarUtil;
import com.dfsek.terra.api.util.logging.DebugLogger;
import com.dfsek.terra.api.util.logging.Logger;
import com.dfsek.terra.config.PluginConfig;
import com.dfsek.terra.config.lang.Language;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.profiler.Profiler;
import com.dfsek.terra.world.TerraWorld;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;


/**
 * Represents a Terra mod/plugin instance.
 */
public interface TerraPlugin extends LoaderRegistrar {
    Logger logger();
    
    boolean reload();
    
    void saveDefaultConfig();
    
    String platformName();
    
    /**
     * Runs a task that may or may not be thread safe, depending on platform.
     * <p>
     * Allows platforms to define what code is safe to be run asynchronously.
     *
     * @param task Task to be run.
     */
    default void runPossiblyUnsafeTask(Runnable task) {
        task.run();
    }
    
    LockedRegistry<TerraAddon> getAddons();
    
    CheckedRegistry<ConfigPack> getConfigRegistry();
    
    File getDataFolder();
    
    DebugLogger getDebugLogger();
    
    EventManager getEventManager();
    
    ItemHandle getItemHandle();
    
    Language getLanguage();
    
    default JarFile getModJar() throws URISyntaxException, IOException {
        return JarUtil.getJarFile();
    }
    
    Profiler getProfiler();
    
    PluginConfig getTerraConfig();
    
    default String getVersion() {
        return "@VERSION@";
    }
    
    TerraWorld getWorld(World world);
    
    WorldHandle getWorldHandle();
}
