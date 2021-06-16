package com.dfsek.terra.api.platform.block.state;

import com.dfsek.terra.api.platform.entity.EntityType;
import org.jetbrains.annotations.NotNull;


public interface MobSpawner extends BlockState {
    int getDelay();
    
    void setDelay(int delay);
    
    int getMaxNearbyEntities();
    
    void setMaxNearbyEntities(int maxNearbyEntities);
    
    int getMaxSpawnDelay();
    
    void setMaxSpawnDelay(int delay);
    
    int getMinSpawnDelay();
    
    void setMinSpawnDelay(int delay);
    
    int getRequiredPlayerRange();
    
    void setRequiredPlayerRange(int requiredPlayerRange);
    
    int getSpawnCount();
    
    void setSpawnCount(int spawnCount);
    
    int getSpawnRange();
    
    void setSpawnRange(int spawnRange);
    
    EntityType getSpawnedType();
    
    void setSpawnedType(@NotNull EntityType creatureType);
}
