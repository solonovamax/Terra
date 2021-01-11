package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.StringTag;

public class FabricStringTag implements StringTag {
    private final net.minecraft.nbt.StringTag delegate;

    public FabricStringTag(net.minecraft.nbt.StringTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.nbt.StringTag getHandle() {
        return delegate;
    }

    @Override
    public StringTag copy() {
        return this;
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
        return delegate.equals(((FabricStringTag) o).delegate);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
