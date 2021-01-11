package com.dfsek.terra.api.platform.nbt;

public interface EndTag extends Tag {
    String toString();

    default byte getType() {
        return 0;
    }

    EndTag copy();


}
