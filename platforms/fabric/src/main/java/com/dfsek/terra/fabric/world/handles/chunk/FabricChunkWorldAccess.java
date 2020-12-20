package com.dfsek.terra.fabric.world.handles.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

public class FabricChunkWorldAccess implements Chunk {
    private final WorldAccess chunkRegion;
    private final int x;
    private final int z;

    public FabricChunkWorldAccess(WorldAccess chunkRegion, int x, int z) {
        this.chunkRegion = chunkRegion;
        this.x = x << 4;
        this.z = z << 4;
    }

    @Override
    public int getX() {
        return x >> 4;
    }

    @Override
    public int getZ() {
        return z >> 4;
    }

    @Override
    public World getWorld() {
        return new FabricWorldAccess(chunkRegion);
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + this.x, y, z + this.z);
        return new FabricBlock(pos, chunkRegion);
    }

    @Override
    public WorldAccess getHandle() {
        return chunkRegion;
    }
}
