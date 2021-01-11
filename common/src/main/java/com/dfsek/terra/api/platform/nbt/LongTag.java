package com.dfsek.terra.api.platform.nbt;

public interface LongTag extends NumberTag {
    String toString();

    default byte getType() {
        return 4;
    }

    LongTag copy();

    int hashCode();

    boolean equals(Object o);
}
