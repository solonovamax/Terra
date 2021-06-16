package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import com.dfsek.terra.bukkit.BukkitEntity;
import com.dfsek.terra.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.bukkit.world.block.BukkitBlock;
import com.dfsek.terra.bukkit.world.entity.BukkitEntityType;

import java.io.File;
import java.util.UUID;


public class BukkitWorld implements World {
    private final org.bukkit.World delegate;
    
    public BukkitWorld(org.bukkit.World delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        return new BukkitEntity(delegate.spawnEntity(BukkitAdapter.adapt(location), ((BukkitEntityType) entityType).getHandle()));
    }
    
    @Override
    public Block getBlockAt(int x, int y, int z) {
        return new BukkitBlock(delegate.getBlockAt(x, y, z));
    }
    
    @Override
    public Chunk getChunkAt(int x, int z) {
        return BukkitAdapter.adapt(delegate.getChunkAt(x, z));
    }
    
    @Override
    public ChunkGenerator getGenerator() {
        return new BukkitChunkGenerator(delegate.getGenerator());
    }
    
    @Override
    public int getMaxHeight() {
        return delegate.getMaxHeight();
    }
    
    @Override
    public int getMinHeight() {
        return 0;
    }
    
    @Override
    public long getSeed() {
        return delegate.getSeed();
    }
    
    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BukkitWorld)) return false;
        BukkitWorld other = (BukkitWorld) obj;
        return other.getHandle().equals(delegate);
    }
    
    @Override
    public String toString() {
        return delegate.toString();
    }
    
    @Override
    public org.bukkit.World getHandle() {
        return delegate;
    }
    
    public String getName() {
        return delegate.getName();
    }
    
    public UUID getUID() {
        return delegate.getUID();
    }
    
    public File getWorldFolder() {
        return delegate.getWorldFolder();
    }
    
    public boolean isChunkGenerated(int x, int z) {
        return delegate.isChunkGenerated(x, z);
    }
}
