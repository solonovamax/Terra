package com.dfsek.terra.bukkit.world.block.data;

import com.dfsek.terra.api.platform.block.data.Rail;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitRail extends BukkitBlockData implements Rail {
    public BukkitRail(org.bukkit.block.data.Rail delegate) {
        super(delegate);
    }
    
    @Override
    public Shape getShape() {
        return BukkitAdapter.adapt(((org.bukkit.block.data.Rail) getHandle()).getShape());
    }
    
    @Override
    public void setShape(Shape newShape) {
        ((org.bukkit.block.data.Rail) getHandle()).setShape(BukkitAdapter.adapt(newShape));
    }
}
