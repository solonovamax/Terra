package com.dfsek.terra.structure;

import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.structure.serialize.SerializableBlockData;
import com.dfsek.terra.structure.serialize.block.SerializableBanner;
import com.dfsek.terra.structure.serialize.block.SerializableBlockState;
import com.dfsek.terra.structure.serialize.block.SerializableMonsterCage;
import com.dfsek.terra.structure.serialize.block.SerializableSign;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;

import java.io.Serializable;

public class StructureContainedBlock implements Serializable {
    private static final long serialVersionUID = 6143969483382710947L;
    private final SerializableBlockData bl;
    private final Pull pull;
    private final int pullOffset;
    private final int x;
    private final int y;
    private final int z;
    private final SerializableBlockState state;
    private final StructureSpawnRequirement requirement;
    private transient BlockData data;

    public StructureContainedBlock(int x, int y, int z, BlockState state, BlockData d, StructureSpawnRequirement spawn, Pull pull, int pullOffset) {
        if(state instanceof Sign) {
            Debug.info("Sign at (" + x + ", " + y + ", " + z + ").");
            this.state = new SerializableSign((org.bukkit.block.Sign) state);
        } else if(state instanceof CreatureSpawner) {
            Debug.info("Monster Spawner at (" + x + ", " + y + ", " + z + ").");
            this.state = new SerializableMonsterCage((CreatureSpawner) state);
        } else if(state instanceof Banner) {
            Debug.info("Banner at (" + x + ", " + y + ", " + z + ").");
            this.state = new SerializableBanner((Banner) state);
        } else this.state = null;
        this.x = x;
        this.y = y;
        this.z = z;
        this.bl = new SerializableBlockData(d);
        this.requirement = spawn;
        this.pull = pull;
        this.pullOffset = pullOffset;
    }

    public StructureContainedBlock(int x, int y, int z, SerializableBlockState state, BlockData d, StructureSpawnRequirement spawn, Pull pull, int pullOffset) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.z = z;
        this.bl = new SerializableBlockData(d);
        this.requirement = spawn;
        this.pull = pull;
        this.pullOffset = pullOffset;
    }

    public StructureSpawnRequirement getRequirement() {
        return requirement;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockData getBlockData() {
        if(data == null) {
            data = bl.getData();
        }
        return data;
    }

    public Pull getPull() {
        return pull;
    }

    public int getPullOffset() {
        return pullOffset;
    }

    public SerializableBlockState getState() {
        return state;
    }

    public enum Pull {
        UP, NONE, DOWN
    }
}
