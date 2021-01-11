package com.dfsek.terra.api.platform.nbt;

public interface StringTag extends Tag {
    String toString();

    default byte getType() {
        return 8;
    }

    StringTag copy();

    String asString();

    int hashCode();

    boolean equals(Object o);
}
