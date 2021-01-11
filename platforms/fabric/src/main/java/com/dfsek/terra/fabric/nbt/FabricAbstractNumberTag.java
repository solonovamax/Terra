package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.NumberTag;
import net.minecraft.nbt.AbstractNumberTag;

public abstract class FabricAbstractNumberTag implements NumberTag {
    protected final AbstractNumberTag delegate;

    protected FabricAbstractNumberTag(AbstractNumberTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public AbstractNumberTag getHandle() {
        return delegate;
    }

    @Override
    public long getLong() {
        return delegate.getLong();
    }

    @Override
    public int getInt() {
        return delegate.getInt();
    }

    @Override
    public short getShort() {
        return delegate.getShort();
    }

    @Override
    public byte getByte() {
        return delegate.getByte();
    }

    @Override
    public double getDouble() {
        return delegate.getDouble();
    }

    @Override
    public float getFloat() {
        return delegate.getFloat();
    }

    @Override
    public Number getNumber() {
        return delegate.getNumber();
    }

    @Override
    public String asString() {
        return delegate.asString();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return delegate.equals(((FabricAbstractNumberTag) o).delegate);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
