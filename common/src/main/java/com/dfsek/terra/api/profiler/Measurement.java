package com.dfsek.terra.api.profiler;

import com.dfsek.terra.api.math.MathUtil;
import com.dfsek.terra.api.util.GlueList;
import net.jafama.FastMath;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to record and hold all data for a single type of measurement performed by the profiler.
 */
public class Measurement {
    private final List<Long> measurements = new LinkedList<>();
    private final long desirable;
    private final DataType type;
    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;

    /**
     * Constructs a new Measurement with a desired value and DataType.
     *
     * @param desirable The desired value of the measurement.
     * @param type      The type of data the measurement is holding.
     */
    public Measurement(long desirable, DataType type) {
        this.desirable = desirable;
        this.type = type;
    }

    public void record(long value) {
        max = FastMath.max(value, max);
        min = FastMath.min(value, min);
        measurements.add(value);
    }

    public int size() {
        return measurements.size();
    }

    public ProfileFuture beginMeasurement() {
        ProfileFuture future = new ProfileFuture();
        long current = System.nanoTime();
        future.thenRun(() -> record(System.nanoTime() - current));
        return future;
    }

    public void reset() {
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        measurements.clear();
    }

    public DataHolder getDataHolder() {
        return new DataHolder(type, desirable, 0.25);
    }

    public long getMin() {
        if(min == Long.MAX_VALUE) return 0;
        return min;
    }

    public long getMax() {
        if(max == Long.MIN_VALUE) return 0;
        return max;
    }

    public long average() {
        BigInteger running = BigInteger.valueOf(0);
        List<Long> mTemp = new GlueList<>(measurements);
        for(Long l : mTemp) {
            running = running.add(BigInteger.valueOf(l));
        }
        if(measurements.size() == 0) return 0;
        return running.divide(BigInteger.valueOf(measurements.size())).longValue();
    }

    public double getStdDev() {
        return MathUtil.standardDeviation(new GlueList<>(measurements));
    }

    public int entries() {
        return measurements.size();
    }

}
