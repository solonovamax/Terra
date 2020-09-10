package com.dfsek.terra.config;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.BlockPalette;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PaletteConfig extends YamlConfiguration {
    private BlockPalette palette;
    private String paletteID;
    private boolean isEnabled = false;
    private String friendlyName;
    public PaletteConfig(File file) throws IOException, InvalidConfigurationException {
        load(file);
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        palette  = new BlockPalette();
        for(Map<?, ?> m : getMapList("blocks")) {
            try {
                ProbabilityCollection<BlockData> layer = new ProbabilityCollection<>();
                for(Map.Entry<String, Integer> type : ((Map<String, Integer>) m.get("materials")).entrySet()) {
                    layer.add(Bukkit.createBlockData(type.getKey()), type.getValue());
                    Bukkit.getLogger().info("[Terra] Added" + type.getKey() + " with probability " + type.getValue());
                }
                Bukkit.getLogger().info("[Terra] Added above materials for " + m.get("layers") + " layers.");
                palette.addBlockData(layer, (Integer) m.get("layers"));
            } catch(ClassCastException e) {
                e.printStackTrace();
            }
        }
        if(!contains("id")) throw new InvalidConfigurationException("Grid ID unspecified!");
        this.paletteID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Grid Name unspecified!");
        this.friendlyName = getString("name");
        isEnabled = true;
    }

    public BlockPalette getPalette() {
        return palette;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getPaletteID() {
        return paletteID;
    }

    private static class Range {
        private final int min;
        private final int max;

        /**
         * Instantiates a Range object with a minimum value (inclusive) and a maximum value (exclusive).
         * @param min The minimum value (inclusive).
         * @param max The maximum value (exclusive).
         */
        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Tests if a value is within range.
         * @param val The value to test.
         * @return boolean - Whether the value is within range.
         */
        public boolean isInRange(int val) {
            return val >= min && val < max;
        }
    }
}