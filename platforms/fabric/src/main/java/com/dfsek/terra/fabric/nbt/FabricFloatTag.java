package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.FloatTag;

public class FabricFloatTag extends FabricAbstractNumberTag implements FloatTag {

    public FabricFloatTag(net.minecraft.nbt.FloatTag delegate) {
        super(delegate);
    }

    @Override
    public net.minecraft.nbt.FloatTag getHandle() {
        return (net.minecraft.nbt.FloatTag) delegate;
    }

    @Override
    public FloatTag copy() {
        return this;
    }
}
