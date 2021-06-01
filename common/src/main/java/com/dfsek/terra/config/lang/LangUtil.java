package com.dfsek.terra.config.lang;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.CommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.jar.JarFile;

import static com.dfsek.terra.api.util.JarUtil.copyResourcesToDirectory;


public final class LangUtil {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Language language;
    
    public static void load(String langID, TerraPlugin main) {
        File langFolder = new File(main.getDataFolder(), "lang");
        try(JarFile jar = main.getModJar()) {
            copyResourcesToDirectory(jar, "lang", langFolder.toString());
        } catch(IOException | URISyntaxException e) {
            logger.error("Failed to dump language files!", e);
        }
        try {
            File langFile = new File(langFolder, langID + ".yml");
            language = new Language(langFile);
            logger.debug("Loaded file at {}.", langFile.getAbsolutePath());
            logger.info("Found {} language files. Loaded language {}.", Objects.requireNonNull(langFolder.listFiles()).length, langID);
        } catch(IOException e) {
            logger.error("Unable to load language: {}.\n" +
                         "Double-check your configuration before reporting this to Terra!", langID, e);
        }
    }
    
    public static Language getLanguage() {
        return language;
    }
    
    public static void send(String messageID, CommandSender sender, String... args) {
        language.getMessage(messageID).send(sender, args);
    }
}
