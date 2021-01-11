package com.dfsek.terra.api.platform.nbt;

import com.dfsek.terra.api.platform.Handle;

public interface Tag extends Handle {
    String toString();

    byte getType();

    Tag copy();

    default String asString() {
        return this.toString();
    }
}
