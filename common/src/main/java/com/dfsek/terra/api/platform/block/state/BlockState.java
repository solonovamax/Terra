package com.dfsek.terra.api.platform.block.state;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;


public interface BlockState extends Handle {
    boolean update(boolean applyPhysics);
    
    default void applyState(String state) {
        // Do nothing by default.
    }
    
    Block getBlock();
    
    BlockData getBlockData();
    
    int getX();
    
    int getY();
    
    int getZ();
}
