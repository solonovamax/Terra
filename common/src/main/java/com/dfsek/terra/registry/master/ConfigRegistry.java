package com.dfsek.terra.registry.master;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.TerraPlugin;
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


/**
 * Class to hold config packs
 */
public class ConfigRegistry extends OpenRegistry<ConfigPack> {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    public boolean loadAll(final TerraPlugin main) {
        boolean valid = true;
        final File packsFolder = new File(main.getDataFolder(), "packs");
        //noinspection ResultOfMethodCallIgnored
        packsFolder.mkdirs();
    
        for(final File file : Objects.requireNonNull(packsFolder.listFiles())) {
            try {
                final ConfigPack pack = new ConfigPack(main, file);
                add(pack.getTemplate().getID(), pack);
            } catch(final FileNotFoundException e) {
                logger.warn("not found?", e);
            } catch(final ZipException e) {
                logger.warn("'{}' is not a valid ZIP archive", file.getName(), e);
            } catch(final ConfigException | IOException e) {
                logger.error("Failed to load config pack '{}'", file.getName(), e);
                valid = false;
            }
        }
        return valid;
    }
}
