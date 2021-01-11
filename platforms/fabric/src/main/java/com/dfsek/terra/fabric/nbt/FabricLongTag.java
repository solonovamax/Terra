package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.LongTag;

public class FabricLongTag extends FabricAbstractNumberTag implements LongTag {

    public FabricLongTag(net.minecraft.nbt.LongTag delegate) {
        super(delegate);
    }

    @Override
    public net.minecraft.nbt.LongTag getHandle() {
        return (net.minecraft.nbt.LongTag) delegate;
    }

    @Override
    public LongTag copy() {
        return this;
    }
}
