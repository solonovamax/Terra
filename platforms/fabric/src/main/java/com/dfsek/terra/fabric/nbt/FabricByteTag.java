package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.ByteTag;

public class FabricByteTag extends FabricAbstractNumberTag implements ByteTag {
    public FabricByteTag(net.minecraft.nbt.ByteTag delegate) {
        super(delegate);
    }

    @Override
    public net.minecraft.nbt.ByteTag getHandle() {
        return (net.minecraft.nbt.ByteTag) delegate;
    }

    @Override
    public ByteTag copy() {
        return this;
    }
}
