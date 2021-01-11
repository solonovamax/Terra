package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.DoubleTag;

public class FabricDoubleTag extends FabricAbstractNumberTag implements DoubleTag {

    public FabricDoubleTag(net.minecraft.nbt.DoubleTag delegate) {
        super(delegate);
    }

    @Override
    public net.minecraft.nbt.DoubleTag getHandle() {
        return (net.minecraft.nbt.DoubleTag) delegate;
    }

    @Override
    public DoubleTag copy() {
        return this;
    }
}
