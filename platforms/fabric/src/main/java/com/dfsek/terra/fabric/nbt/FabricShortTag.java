package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.ShortTag;

public class FabricShortTag extends FabricAbstractNumberTag implements ShortTag {
    public FabricShortTag(net.minecraft.nbt.ShortTag delegate) {
        super(delegate);
    }

    @Override
    public net.minecraft.nbt.ShortTag getHandle() {
        return (net.minecraft.nbt.ShortTag) delegate;
    }

    @Override
    public ShortTag copy() {
        return this;
    }
}
