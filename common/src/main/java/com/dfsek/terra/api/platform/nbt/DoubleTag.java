package com.dfsek.terra.api.platform.nbt;

public interface DoubleTag extends NumberTag {
    String toString();

    default byte getType() {
        return 6;
    }

    DoubleTag copy();

    int hashCode();

    boolean equals(Object o);


}
