package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.Debug;
import com.dfsek.terra.config.base.ConfigPack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
 * Class to hold config packs
 */
public class ConfigRegistry extends TerraRegistry<ConfigPack> {
    private static ConfigRegistry singleton;

    private ConfigRegistry() {

    }

    public static boolean loadAll(JavaPlugin main) {
        boolean valid = true;
        File packsFolder = new File(main.getDataFolder(), "packs");
        for(File dir : packsFolder.listFiles(File::isDirectory)) {
            try {
                getRegistry().load(dir);
            } catch(ConfigException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        for(File zip : packsFolder.listFiles(file -> file.getName().endsWith(".zip") || file.getName().endsWith(".jar") || file.getName().endsWith(".terra"))) {
            try {
                String fileName = zip.getName();

                Pattern pattern = Pattern.compile(".*\\.(.*)");
                Matcher matcher = pattern.matcher(fileName);
                if(matcher.find()) {
                    switch(matcher.group(1)) {
                        case "zip":
                            Debug.info("Loading ZIP archive: " + zip.getName());
                            getRegistry().load(new ZipFile(zip));
                            break;
                        case "terra":
                        case "jar":
                            Debug.info("Loading JAR or TERRA archive: " + zip.getName());
                            getRegistry().load(new JarFile(zip));
                            break;
                        default:
                            Debug.error("Zip matched .zip, .jar, or .terra, but did not fit into .zip, .jar. or .terra. " +
                                    "This error should never happen. If it did, either you, or me, fucked up badly.");
                            valid = false;
                    }
                }
            } catch(IOException | ConfigException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        return valid;
    }

    public static ConfigRegistry getRegistry() {
        if(singleton == null) singleton = new ConfigRegistry();
        return singleton;
    }

    public void load(File folder) throws ConfigException {
        ConfigPack pack = new ConfigPack(folder);
        add(pack.getTemplate().getID(), pack);
    }


    public void load(ZipFile file) throws ConfigException {
        ConfigPack pack = new ConfigPack(file);
        add(pack.getTemplate().getID(), pack);
    }

    public void load(JarFile file) throws ConfigException {
        ConfigPack pack = new ConfigPack(file);
        add(pack.getTemplate().getID(), pack);
    }
}
