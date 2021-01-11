package com.dfsek.terra.api.platform.nbt;

public interface FloatTag extends NumberTag {
    String toString();

    default byte getType() {
        return 5;
    }

    FloatTag copy();

    int hashCode();

    boolean equals(Object o);


}
