package com.dfsek.terra.addons;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.config.files.ZIPLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

/**
 * Loads a jar file & then uses that for
 *
 * @author solonovamax
 */
public class JarLoader extends ZIPLoader {
    public JarLoader(JarFile file) throws LoadException {
        super(file);
        try {
            InputStream is = get("addon.yml");

            ConfigLoader cf = new ConfigLoader();
            AddonYml yml = new AddonYml();
            cf.load(yml, is);
            String className = yml.getMainClass();
        } catch(IOException | ConfigException e) {
            throw new LoadException("Could not load addon.yml", e);
        }
    }
}
