package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.IntTag;

public class FabricIntTag extends FabricAbstractNumberTag implements IntTag {

    public FabricIntTag(net.minecraft.nbt.IntTag delegate) {
        super(delegate);
    }

    @Override
    public net.minecraft.nbt.IntTag getHandle() {
        return (net.minecraft.nbt.IntTag) delegate;
    }

    @Override
    public IntTag copy() {
        return this;
    }
}
