package com.dfsek.terra.api.platform.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;


public interface World extends Handle {
    Entity spawnEntity(Location location, EntityType entityType);
    
    Block getBlockAt(int x, int y, int z);
    
    default Block getBlockAt(Location l) {
        return getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }
    
    Chunk getChunkAt(int x, int z);
    
    default Chunk getChunkAt(Location location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }
    
    ChunkGenerator getGenerator();
    
    int getMaxHeight();
    
    int getMinHeight();
    
    long getSeed();
    
    default TerraChunkGenerator getTerraGenerator() {
        return ((GeneratorWrapper) getGenerator().getHandle()).getHandle();
    }
    
    default boolean isTerraWorld() {
        return getGenerator().getHandle() instanceof GeneratorWrapper;
    }
}
