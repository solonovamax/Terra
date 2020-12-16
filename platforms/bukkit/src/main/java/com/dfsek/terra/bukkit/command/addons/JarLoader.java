package com.dfsek.terra.addons;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.terra.Terra;
import com.dfsek.terra.config.files.ZIPLoader;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.jar.JarFile;

/**
 * Loads a jar file & then uses that for
 *
 * @author solonovamax
 */
public class JarLoader extends ZIPLoader {
    private final JarFile file;

    public JarLoader(JarFile file) throws LoadException {
        super(file);
        this.file = file;
        try {
            InputStream is = get("addon.yml");

            ConfigLoader cf = new ConfigLoader();
            AddonYml yml = new AddonYml();
            cf.load(yml, is);
            String className = yml.getMainClass();

            initializePlugin(className);

        } catch(IOException | ConfigException e) {
            throw new LoadException("Could not load addon.yml", e);
        }
    }

    public void initializePlugin(String mainClassName) {
        Terra terra = Terra.getInstance();
        JarClassLoader jcl = new JarClassLoader(Collections.singletonList(file.getName()));
        JclObjectFactory factory = JclObjectFactory.getInstance();

        Addon addon = (Addon) JclUtils.toCastable(factory.create(jcl, mainClassName), Addon.class);

        addon.initialize(terra);
    }
}
