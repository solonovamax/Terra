package com.dfsek.terra.registry.master;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.config.fileloaders.FolderLoader;
import com.dfsek.terra.config.fileloaders.ZIPLoader;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.registry.OpenRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * Class to hold config packs
 */
public class ConfigRegistry extends OpenRegistry<ConfigPack> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    
    public boolean loadAll(TerraPlugin main) {
        boolean valid = true;
        File packsFolder = new File(main.getDataFolder(), "packs");
        //noinspection ResultOfMethodCallIgnored
        packsFolder.mkdirs();
        
        for(File file : Objects.requireNonNull(packsFolder.listFiles())) {
            try {
                if(file.isDirectory()) {
                    logger.info("Attempting to load config pack from folder '{}'.", file.getName());
                    
                    ConfigPack pack = new ConfigPack(main, new FolderLoader(file.toPath()));
                    add(pack.getTemplate().getID(), pack);
                } else if(file.getName().endsWith(".zip") || file.getName().endsWith(".terra")) {
                    logger.info("Attempting to load config pack from ZIP archive '{}'.", file.getName());
                    
                    ConfigPack pack = new ConfigPack(main, new ZIPLoader(new ZipFile(file)));
                    add(pack.getTemplate().getID(), pack);
                }
            } catch(FileNotFoundException e) {
            
            } catch(ZipException e) {
                logger.warn("'{}' is not a valid ZIP archive", file.getName(), e);
            } catch(ConfigException | IOException e) {
                logger.error("Failed to load config pack '{}'", file.getName(), e);
                valid = false;
            }
        }
        return valid;
    }
}
