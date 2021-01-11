package com.dfsek.terra.api.platform.nbt;

public interface ShortTag extends NumberTag {
    String toString();

    default byte getType() {
        return 2;
    }

    ShortTag copy();

    int hashCode();

    boolean equals(Object o);
}
