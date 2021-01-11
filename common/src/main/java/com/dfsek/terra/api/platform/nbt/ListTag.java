package com.dfsek.terra.api.platform.nbt;

import java.util.List;

public interface ListTag extends List<Tag>, Tag {
    @Override
    default byte getType() {
        return 9;
    }

    ListTag copy();

    CompoundTag getCompound(int index);

    ListTag getList(int index);

    short getShort(int index);

    int getInt(int i);

    int[] getIntArray(int index);

    double getDouble(int index);

    float getFloat(int index);

    String getString(int index);

    int size();

    boolean isEmpty();

    void clear();

    boolean equals(Object o);

    int hashCode();

    Tag get(int i);

    void add(int i, Tag tag);

    Tag remove(int i);

    boolean setTag(int index, Tag tag);

    boolean addTag(int index, Tag tag);

    boolean canAdd(Tag tag);

    byte getElementType();
}
