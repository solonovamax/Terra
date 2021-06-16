package com.dfsek.terra.api.structures.structure.buffer.items;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


public class BufferedStateManipulator implements BufferedItem {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TerraPlugin main;
    private final String data;
    
    public BufferedStateManipulator(TerraPlugin main, String state) {
        this.main = main;
        this.data = state;
    }
    
    @Override
    public void paste(Location origin) {
        try {
            BlockState state = origin.getBlock().getState();
            state.applyState(data);
            state.update(false);
        } catch(Exception e) {
            logger.warn("Could not apply BlockState at {}.", origin, e);
        }
    }
}
