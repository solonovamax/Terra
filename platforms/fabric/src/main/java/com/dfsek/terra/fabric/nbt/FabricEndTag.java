package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.EndTag;

public class FabricEndTag implements EndTag {
    public static final FabricEndTag INSTANCE = new FabricEndTag(net.minecraft.nbt.EndTag.INSTANCE);
    private final net.minecraft.nbt.EndTag delegate;

    private FabricEndTag(net.minecraft.nbt.EndTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object getHandle() {
        return delegate;
    }

    @Override
    public EndTag copy() {
        return this;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return delegate.equals(((FabricEndTag) o).delegate);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
