package com.dfsek.terra.api.platform.nbt;

import java.util.List;

public interface LongArrayTag extends List<LongTag>, Tag {
    default byte getType() {
        return 12;
    }

    LongArrayTag copy();

    String toString();

    long[] getLongArray();

    int size();

    void clear();

    boolean equals(Object o);

    int hashCode();

    LongTag get(int i);

    /**
     * Note: this method is called "method_10606" in the fabric jar I have for some reason.
     *
     * @param i
     * @param longTag
     * @return
     */
    LongTag set(int i, LongTag longTag);

    void add(int i, LongTag longTag);

    LongTag remove(int i);

    boolean setTag(int index, Tag tag);

    boolean addTag(int index, Tag tag);

    default byte getElementType() {
        return 4;
    }
}
