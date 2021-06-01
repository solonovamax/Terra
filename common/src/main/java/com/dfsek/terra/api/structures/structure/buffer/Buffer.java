package com.dfsek.terra.api.structures.structure.buffer;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.structures.structure.buffer.items.BufferedItem;


public interface Buffer {
    Buffer addItem(BufferedItem item, Location location);
    
    Buffer setMark(String mark, Location location);
    
    String getMark(Location location);
    
    Location getOrigin();
}
