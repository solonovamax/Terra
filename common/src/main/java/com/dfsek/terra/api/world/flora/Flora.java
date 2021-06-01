package com.dfsek.terra.api.world.flora;

import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.world.Chunk;

import java.util.List;


public interface Flora {
    boolean plant(Location l);
    
    List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range check);
}
