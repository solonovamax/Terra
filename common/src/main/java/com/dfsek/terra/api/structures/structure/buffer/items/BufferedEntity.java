package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.entity.EntityType;

public class BufferedEntity implements BufferedItem {

    private final EntityType type;

    public BufferedEntity(EntityType type) {
        this.type = type;
    }

    @Override
    public void paste(Location origin) {
        origin.getWorld().spawnEntity(origin, type);
    }
}
