package com.dfsek.terra.population.items;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.world.Chunk;

import java.util.Random;

public abstract class PlaceableLayer<T> {
    protected final double density;
    protected final Range level;
    protected final ProbabilityCollection<T> layer;
    protected final NoiseSampler noise;

    public PlaceableLayer(double density, Range level, ProbabilityCollection<T> layer, NoiseSampler noise) {
        this.density = density;
        this.level = level;
        this.layer = layer;
        this.noise = noise;
    }

    public NoiseSampler getNoise() {
        return noise;
    }

    public double getDensity() {
        return density;
    }

    public Range getLevel() {
        return level;
    }

    public ProbabilityCollection<T> getLayer() {
        return layer;
    }

    public abstract void place(Chunk chunk, Vector2 coords, Random random);
}
