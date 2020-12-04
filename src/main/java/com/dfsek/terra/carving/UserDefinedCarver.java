package com.dfsek.terra.carving;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.config.templates.CarverTemplate;
import net.jafama.FastMath;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.MathUtil;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.util.FastRandom;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.world.carving.Carver;
import org.polydev.gaea.world.carving.Worm;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class UserDefinedCarver extends Carver {
    private final double[] start; // 0, 1, 2 = x, y, z.
    private final double[] mutate; // 0, 1, 2 = x, y, z. 3 = radius.
    private final Range length;
    private final int hash;
    private final int topCut;
    private final int bottomCut;
    private double step = 2;
    private Range recalc = new Range(8, 10);
    private double recalcMagnitude = 3;
    private final CarverTemplate config;
    private final Expression xRad;
    private final Expression yRad;
    private final Expression zRad;
    private final Variable lengthVar;
    private final Variable position;
    private final Range height;
    private final double sixtyFourSq = FastMath.pow(64, 2);

    public UserDefinedCarver(Range height, Range length, double[] start, double[] mutate, List<String> radii, Scope parent, int hash, int topCut, int bottomCut, CarverTemplate config) throws ParseException {
        super(height.getMin(), height.getMax());
        this.length = length;
        this.height = height;
        this.start = start;
        this.mutate = mutate;
        this.hash = hash;
        this.topCut = topCut;
        this.bottomCut = bottomCut;
        this.config = config;

        Parser p = new Parser();
        Scope s = new Scope().withParent(parent);

        lengthVar = s.create("length");
        position = s.create("position");

        xRad = p.parse(radii.get(0), s);
        yRad = p.parse(radii.get(1), s);
        zRad = p.parse(radii.get(2), s);

    }

    @Override
    public Worm getWorm(long l, Vector vector) {
        Random r = new FastRandom(l + hash);
        return new UserDefinedWorm(length.get(r) / 2, r, vector, topCut, bottomCut);
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setRecalc(Range recalc) {
        this.recalc = recalc;
    }

    @Override
    public void carve(int chunkX, int chunkZ, World w, BiConsumer<Vector, CarvingType> consumer) {
        int carvingRadius = getCarvingRadius();
        TerraBiomeGrid grid = TerraWorld.getWorld(w).getGrid();
        for(int x = chunkX - carvingRadius; x <= chunkX + carvingRadius; x++) {
            z:
            for(int z = chunkZ - carvingRadius; z <= chunkZ + carvingRadius; z++) {
                if(isChunkCarved(w, x, z, new FastRandom(MathUtil.hashToLong(this.getClass().getName() + "_" + x + "&" + z)))) {
                    long seed = MathUtil.getCarverChunkSeed(x, z, w.getSeed());
                    Random r = new FastRandom(seed);
                    Worm carving = getWorm(seed, new Vector((x << 4) + r.nextInt(16), height.get(r), (z << 4) + r.nextInt(16)));
                    Vector origin = carving.getOrigin();
                    List<Worm.WormPoint> points = new GlueList<>();
                    for(int i = 0; i < carving.getLength(); i++) {
                        carving.step();
                        Biome biome = grid.getBiome(carving.getRunning().toLocation(w), GenerationPhase.POPULATE);
                        if(!((UserDefinedBiome) biome).getConfig().getCarvers().containsKey(this)) { // Stop if we enter a biome this carver is not present in
                            continue z;
                        }
                        if(FastMath.floorDiv(origin.getBlockX(), 16) != chunkX && FastMath.floorDiv(origin.getBlockZ(), 16) != chunkZ) { // Only carve in the current chunk.
                            continue;
                        }
                        points.add(carving.getPoint());
                    }
                    points.forEach(point -> point.carve(chunkX, chunkZ, consumer));
                }
            }
        }
    }

    public void setRecalcMagnitude(double recalcMagnitude) {
        this.recalcMagnitude = recalcMagnitude;
    }

    @Override
    public boolean isChunkCarved(World w, int chunkX, int chunkZ, Random random) {
        BiomeTemplate conf = ((UserDefinedBiome) TerraWorld.getWorld(w).getGrid().getBiome(chunkX << 4, chunkZ << 4, GenerationPhase.POPULATE)).getConfig();
        if(conf.getCarvers().get(this) != null) {
            return new FastRandom(random.nextLong() + hash).nextInt(100) < conf.getCarvers().get(this);
        }
        return false;
    }

    public CarverTemplate getConfig() {
        return config;
    }

    private class UserDefinedWorm extends Worm {
        private final Vector direction;
        private int steps;
        private int nextDirection = 0;
        private double[] currentRotation = new double[3];

        public UserDefinedWorm(int length, Random r, Vector origin, int topCut, int bottomCut) {
            super(length, r, origin);
            super.setTopCut(topCut);
            super.setBottomCut(bottomCut);
            direction = new Vector((r.nextDouble() - 0.5D) * start[0], (r.nextDouble() - 0.5D) * start[1], (r.nextDouble() - 0.5D) * start[2]).normalize().multiply(step);
            position.setValue(0);
            lengthVar.setValue(length);
            setRadius(new int[] {(int) (xRad.evaluate()), (int) (yRad.evaluate()), (int) (zRad.evaluate())});
        }

        @Override
        public WormPoint getPoint() {
            return new WormPoint(getRunning().clone(), getRadius(), config.getCutTop(), config.getCutBottom());
        }

        @Override
        public void step() {
            if(steps == nextDirection) {
                direction.rotateAroundX(FastMath.toRadians((getRandom().nextGaussian()) * mutate[0] * recalcMagnitude));
                direction.rotateAroundY(FastMath.toRadians((getRandom().nextGaussian()) * mutate[1] * recalcMagnitude));
                direction.rotateAroundZ(FastMath.toRadians((getRandom().nextGaussian()) * mutate[2] * recalcMagnitude));
                currentRotation = new double[] {(getRandom().nextGaussian()) * mutate[0],
                        (getRandom().nextGaussian()) * mutate[1],
                        (getRandom().nextGaussian()) * mutate[2]};
                nextDirection += recalc.get(getRandom());
            }
            steps++;
            position.setValue(steps);
            setRadius(new int[] {(int) (xRad.evaluate()), (int) (yRad.evaluate()), (int) (zRad.evaluate())});
            direction.rotateAroundX(FastMath.toRadians(currentRotation[0] * mutate[0]));
            direction.rotateAroundY(FastMath.toRadians(currentRotation[1] * mutate[1]));
            direction.rotateAroundZ(FastMath.toRadians(currentRotation[2] * mutate[2]));
            getRunning().add(direction);
        }
    }
}
