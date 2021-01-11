package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.Tag;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;

public class FabricTagHelper {
    // no fun allowed.
    private FabricTagHelper() {
    }

    public static Tag getAppropriateTag(net.minecraft.nbt.Tag mojangTag) {
        if(mojangTag instanceof ByteArrayTag)
            return new FabricByteArrayTag((ByteArrayTag) mojangTag);
        else if(mojangTag instanceof ByteTag)
            return new FabricByteTag((ByteTag) mojangTag);
        else if(mojangTag instanceof net.minecraft.nbt.CompoundTag)
            return new FabricCompoundTag((net.minecraft.nbt.CompoundTag) mojangTag);
        else if(mojangTag instanceof DoubleTag)
            return new FabricDoubleTag((DoubleTag) mojangTag);
        else if(mojangTag instanceof EndTag)
            return FabricEndTag.INSTANCE;
        else if(mojangTag instanceof FloatTag)
            return new FabricFloatTag((FloatTag) mojangTag);
        else if(mojangTag instanceof IntArrayTag)
            return new FabricIntArrayTag((IntArrayTag) mojangTag);
        else if(mojangTag instanceof IntTag)
            return new FabricIntTag((IntTag) mojangTag);
        else if(mojangTag instanceof ListTag)
            return new FabricListTag((ListTag) mojangTag);
        else if(mojangTag instanceof LongArrayTag)
            return new FabricLongArrayTag((LongArrayTag) mojangTag);
        else if(mojangTag instanceof LongTag)
            return new FabricLongTag((LongTag) mojangTag);
        else if(mojangTag instanceof ShortTag)
            return new FabricShortTag((ShortTag) mojangTag);
        else if(mojangTag instanceof StringTag)
            return new FabricStringTag((StringTag) mojangTag);
        else
            throw new IllegalArgumentException(String.format("Could not find appropriate class to cast the tag {%s} to. (Type: \"%s\")", mojangTag.toString(), mojangTag.getClass().getName()));
    }
}
