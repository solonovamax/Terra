package com.dfsek.terra.api.platform.nbt;

public interface NumberTag extends Tag {
    long getLong();

    int getInt();

    short getShort();

    byte getByte();

    double getDouble();

    float getFloat();

    Number getNumber();
}
