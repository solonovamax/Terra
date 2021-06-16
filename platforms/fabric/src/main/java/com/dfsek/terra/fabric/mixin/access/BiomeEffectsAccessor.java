package com.dfsek.terra.fabric.mixin.access;

import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;


@Mixin(BiomeEffects.class)
public interface BiomeEffectsAccessor {
    @Accessor("fogColor")
    int getFogColor();
    
    @Accessor("foliageColor")
    Optional<Integer> getFoliageColor();
    
    @Accessor("grassColor")
    Optional<Integer> getGrassColor();
    
    @Accessor("grassColorModifier")
    BiomeEffects.GrassColorModifier getGrassColorModifier();
    
    @Accessor("skyColor")
    int getSkyColor();
    
    @Accessor("waterColor")
    int getWaterColor();
    
    @Accessor("waterFogColor")
    int getWaterFogColor();
}
