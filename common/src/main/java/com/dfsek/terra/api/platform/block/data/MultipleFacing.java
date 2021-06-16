package com.dfsek.terra.api.platform.block.data;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.BlockFace;

import java.util.Set;


public interface MultipleFacing extends BlockData {
    void setFace(BlockFace face, boolean facing);
    
    Set<BlockFace> getAllowedFaces();
    
    Set<BlockFace> getFaces();
    
    boolean hasFace(BlockFace f);
}
