package com.dfsek.terra.api.world.biome;

import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.world.palette.Palette;


public interface Generator {
    /**
     * Gets the noise sampler instance to use for base terrain.
     *
     * @return NoiseSampler for terrain
     */
    NoiseSampler getBaseSampler();
    
    NoiseSampler getBiomeNoise();
    
    int getBlendDistance();
    
    int getBlendStep();
    
    /**
     * Gets the noise sampler to use for carving.
     *
     * @return NoiseSampler for carving.
     */
    NoiseSampler getCarver();
    
    /**
     * Gets the noise sampler to use for elevation
     *
     * @return NoiseSampler for elevation.
     */
    NoiseSampler getElevationSampler();
    
    double getElevationWeight();
    
    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    Palette getPalette(int y);
    
    double getWeight();
}
