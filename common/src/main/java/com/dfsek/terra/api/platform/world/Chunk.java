package com.dfsek.terra.api.platform.world;

import com.dfsek.terra.api.platform.block.Block;


public interface Chunk extends ChunkAccess {
    Block getBlock(int x, int y, int z);
    
    World getWorld();
    
    int getX();
    
    int getZ();
}
