package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.debug.Debug;

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
    public boolean loadAll(TerraPlugin main) {
        boolean valid = true;
        File packsFolder = new File(main.getDataFolder(), "packs");
        packsFolder.mkdirs();
        for(File dir : packsFolder.listFiles(File::isDirectory)) {
            try {
                load(dir, main);
            } catch(ConfigException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        for(File zip : packsFolder.listFiles(file -> file.getName().endsWith(".zip") || file.getName().endsWith(".jar") || file.getName().endsWith(".com.dfsek.terra"))) {
            try {
                String fileName = zip.getName();

                Pattern pattern = Pattern.compile(".*\\.(.*)");
                Matcher matcher = pattern.matcher(fileName);
                if(matcher.find()) {
                    switch(matcher.group(1)) {
                        case "zip":
                            Debug.info("Loading ZIP archive: " + zip.getName());
                            load(new ZipFile(zip), main);
                            break;
                        case "terra":
                        case "jar":
                            Debug.info("Loading JAR or TERRA archive: " + zip.getName());
                            load(new JarFile(zip));
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

    public void load(File folder, TerraPlugin main) throws ConfigException {
        ConfigPack pack = new ConfigPack(folder, main);
        add(pack.getTemplate().getID(), pack);
    }

    public void load(ZipFile file, TerraPlugin main) throws ConfigException {
        ConfigPack pack = new ConfigPack(file, main);
        add(pack.getTemplate().getID(), pack);
    }

    public void load(JarFile file, TerraPlugin main) throws ConfigException {
        ConfigPack pack = new ConfigPack(file, main);
        add(pack.getTemplate().getID(), pack);
    }
}
