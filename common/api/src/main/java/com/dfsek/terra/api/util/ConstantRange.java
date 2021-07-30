package com.dfsek.terra.api.util;

import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;

public class ConstantRange implements Range {
    private int min;
    private int max;

    public ConstantRange(int min, int max) {
        if(min > max) throw new IllegalArgumentException("Minimum must not be grater than maximum!");
        this.max = max;
        this.min = min;
    }

    @Override
    public boolean isInRange(int test) {
        return test >= min && test < max;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public Range setMax(int max) {
        this.max = max;
        return this;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public Range setMin(int min) {
        this.min = min;
        return this;
    }

    @Override
    public int getRange() {
        return max - min;
    }

    @Override
    public Range multiply(int mult) {
        min *= mult;
        max *= mult;
        return this;
    }

    @Override
    public Range reflect(int pt) {
        return new ConstantRange(2 * pt - this.getMax(), 2 * pt - this.getMin());
    }

    @Override
    public int get(Random r) {
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    public Range intersects(Range other) {
        try {
            return new ConstantRange(FastMath.max(this.getMin(), other.getMin()), FastMath.min(this.getMax(), other.getMax()));
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Range add(int add) {
        this.min += add;
        this.max += add;
        return this;
    }

    @Override
    public Range sub(int sub) {
        this.min -= sub;
        this.max -= sub;
        return this;
    }

    @Override
    public String toString() {
        return "Min: " + getMin() + ", Max:" + getMax();
    }

    @Override
    public int hashCode() {
        return min * 31 + max;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ConstantRange)) return false;
        Range other = (Range) obj;
        return other.getMin() == this.getMin() && other.getMax() == this.getMax();
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator(this);
    }

    private static class RangeIterator implements Iterator<Integer> {
        private final Range m;
        private Integer current;

        public RangeIterator(Range m) {
            this.m = m;
            current = m.getMin();
        }

        @Override
        public boolean hasNext() {
            return current < m.getMax();
        }

        @Override
        public Integer next() {
            current++;
            return current - 1;
        }
    }
}
