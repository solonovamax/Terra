package com.dfsek.terra.api.platform.nbt;

public interface IntTag extends NumberTag {
    String toString();

    default byte getType() {
        return 3;
    }

    IntTag copy();

    int hashCode();

    boolean equals(Object o);


}
