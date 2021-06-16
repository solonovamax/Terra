package com.dfsek.terra.api.platform.block;

import com.dfsek.terra.api.platform.Handle;


public interface BlockData extends Cloneable, Handle {
    
    boolean matches(BlockData other);
    
    BlockData clone();
    
    String getAsString();
    
    BlockType getBlockType();
    
    boolean isAir();
    
    boolean isStructureVoid();
}
