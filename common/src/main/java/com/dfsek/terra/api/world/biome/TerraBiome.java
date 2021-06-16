package com.dfsek.terra.api.world.biome;


import com.dfsek.terra.api.platform.world.Biome;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;

import java.util.Set;


/**
 * Represents a custom biome
 */
public interface TerraBiome {
    
    int getColor();
    
    /**
     * Gets the BiomeTerrain instance used to generate the biome.
     *
     * @return BiomeTerrain - The terrain generation instance.
     */
    Generator getGenerator(World w);
    
    String getID();
    
    Set<String> getTags();
    
    /**
     * Gets the Vanilla biome to represent the custom biome.
     *
     * @return TerraBiome - The Vanilla biome.
     */
    ProbabilityCollection<Biome> getVanillaBiomes();
}
