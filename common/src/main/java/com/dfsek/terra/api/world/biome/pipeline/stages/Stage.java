package com.dfsek.terra.api.world.biome.pipeline.stages;

import com.dfsek.terra.api.world.biome.pipeline.BiomeHolder;


public interface Stage {
    BiomeHolder apply(BiomeHolder in);
    
    boolean isExpansion();
    
}
