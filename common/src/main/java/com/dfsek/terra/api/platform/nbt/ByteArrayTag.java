package com.dfsek.terra.api.platform.nbt;

import java.util.List;

public interface ByteArrayTag extends List<ByteTag>, Tag {
    default byte getType() {
        return 7;
    }

    byte[] getByteArray();

    int size();

    void clear();

    boolean equals(Object o);

    int hashCode();

    ByteTag get(int i);

    ByteTag set(int i, ByteTag byteTag);

    /**
     * Note: this is "method_10531" on the fabric jar I have (will have to check if this isn't a glitch)
     *
     * @param i
     * @param byteTag
     */
    void add(int i, ByteTag byteTag);

    /**
     * Note: this is "method_10536" on the fabric jar I have (will have to check if this isn't a glitch)
     *
     * @param i
     * @return
     */
    ByteTag remove(int i);

    boolean setTag(int index, Tag tag);

    boolean addTag(int index, Tag tag);

    default byte getElementType() {
        return 1;
    }
}
