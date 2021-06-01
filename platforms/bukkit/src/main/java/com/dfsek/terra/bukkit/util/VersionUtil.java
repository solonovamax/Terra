package com.dfsek.terra.bukkit.util;

import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class VersionUtil {
    private static final Logger logger = LoggerFactory.getLogger(VersionUtil.class);
    public static final SpigotVersionInfo SPIGOT_VERSION_INFO = new SpigotVersionInfo();
    public static final MinecraftVersionInfo MINECRAFT_VERSION = new MinecraftVersionInfo();
    
    public static MinecraftVersionInfo getMinecraftVersion() {
        return MINECRAFT_VERSION;
    }
    
    public static SpigotVersionInfo getSpigotVersionInfo() {
        return SPIGOT_VERSION_INFO;
    }
    
    public static final class SpigotVersionInfo {
        private final boolean isSpigot;
        private final boolean isPaper;
        private final boolean isAirplane;
        private final boolean isTuinity;
        private final boolean isPurpur;
        private final boolean isYaptopia;
        
        public SpigotVersionInfo() {
            
            isPaper = PaperLib.isPaper();
            isSpigot = PaperLib.isSpigot();
            
            boolean isTuinity = false;
            try {
                Class.forName("com.tuinity.tuinity.config.TuinityConfig");
                isTuinity = true;
            } catch(ClassNotFoundException ignored) {}
            this.isTuinity = isTuinity;
            
            boolean isAirplane = false;
            try {
                Class.forName("gg.airplane.AirplaneConfig");
                isAirplane = true;
            } catch(ClassNotFoundException ignored) {}
            this.isAirplane = isAirplane;
            
            boolean isPurpur = false;
            try {
                Class.forName("net.pl3x.purpur.PurpurConfig");
                isPurpur = true;
            } catch(ClassNotFoundException ignored) {}
            this.isPurpur = isPurpur;
            
            boolean isYaptopia = false;
            try {
                Class.forName("org.yatopiamc.yatopia.server.YatopiaConfig");
                isYaptopia = true;
            } catch(ClassNotFoundException ignored) {}
            this.isYaptopia = isYaptopia;
        }
        
        @Override
        public String toString() {
            if(isYaptopia) return "Yaptopia";
            else if(isPurpur) return "Purpur";
            else if(isTuinity) return "Tuinity";
            else if(isAirplane) return "Airplane";
            else if(isPaper) return "Paper";
            else if(isSpigot) return "Spigot";
            else return "Craftbukkit";
        }
        
        public boolean isAirplane() {
            return isAirplane;
        }
        
        public boolean isPaper() {
            return isPaper;
        }
        
        public boolean isPurpur() {
            return isPurpur;
        }
        
        public boolean isSpigot() {
            return isSpigot;
        }
        
        public boolean isTuinity() {
            return isTuinity;
        }
        
        public boolean isYaptopia() {
            return isYaptopia;
        }
    }
    
    
    public static final class MinecraftVersionInfo {
        private static final Logger logger = LoggerFactory.getLogger(VersionUtil.class);
        private static final Pattern versionPattern = Pattern.compile("v?(\\d+)_(\\d+)_R(\\d+)");
        private final int major;
        private final int minor;
        private final int patch;
        
        public MinecraftVersionInfo(int major, int minor, int patch) {
            this.major = major;
            this.minor = minor;
            this.patch = patch;
        }
        
        public MinecraftVersionInfo(String versionString) {
            Matcher versionMatcher = versionPattern.matcher(versionString);
            if(versionMatcher.find()) {
                major = Integer.parseInt(versionMatcher.group(1));
                minor = Integer.parseInt(versionMatcher.group(2));
                patch = Integer.parseInt(versionMatcher.group(3));
            } else {
                major = -1;
                minor = -1;
                patch = -1;
            }
        }
        
        public MinecraftVersionInfo() {
            this(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]);
        }
        
        @Override
        public String toString() {
            return String.format("v%d.%d.%d", major, minor, patch);
        }
        
        public int getMajor() {
            return major;
        }
        
        public int getMinor() {
            return minor;
        }
        
        public int getPatch() {
            return patch;
        }
    }
}
