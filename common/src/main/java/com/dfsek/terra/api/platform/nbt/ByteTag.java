package com.dfsek.terra.api.platform.nbt;

public interface ByteTag extends NumberTag {
    String toString();

    default byte getType() {
        return 1;
    }

    ByteTag copy();

    int hashCode();

    boolean equals(Object o);


}
