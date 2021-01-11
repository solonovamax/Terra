package com.dfsek.terra.api.platform.nbt;

import java.util.List;

public interface IntArrayTag extends List<IntTag>, Tag {
    default byte getType() {
        return 11;
    }

    IntArrayTag copy();

    String toString();

    int[] getIntArray();

    int size();

    void clear();

    boolean equals(Object o);

    int hashCode();

    IntTag get(int i);

    IntTag set(int i, IntTag intTag);

    void add(int i, IntTag intTag);

    IntTag remove(int i);

    boolean setTag(int index, Tag tag);

    boolean addTag(int index, Tag tag);

    default byte getElementType() {
        return 3;
    }
}
