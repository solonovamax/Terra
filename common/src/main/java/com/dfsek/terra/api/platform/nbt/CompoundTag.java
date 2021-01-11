package com.dfsek.terra.api.platform.nbt;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface CompoundTag extends Tag {
    @Override
    default byte getType() {
        return 10;
    }

    CompoundTag copy();

    Set<String> getKeys();

    int getSize();

    Tag put(String key, Tag tag);

    void putByte(String key, byte value);

    void putShort(String key, short value);

    void putInt(String key, int value);

    void putLong(String key, long value);

    void putUuid(String key, UUID value);

    UUID getUuid(String key);

    boolean containsUuid(String key);

    void putFloat(String key, float value);

    void putDouble(String key, double value);

    void putString(String key, String value);

    void putByteArray(String key, byte[] value);

    void putIntArray(String key, int[] value);

    void putIntArray(String key, List<Integer> value);

    void putLongArray(String key, long[] value);

    void putLongArray(String key, List<Long> value);

    void putBoolean(String key, boolean value);

    Tag get(String key);

    byte getType(String key);

    boolean contains(String key);

    boolean contains(String key, int type);

    byte getByte(String key);

    short getShort(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    double getDouble(String key);

    String getString(String key);

    byte[] getByteArray(String key);

    int[] getIntArray(String key);

    long[] getLongArray(String key);

    CompoundTag getCompound(String key);

    ListTag getList(String key, int type);

    boolean getBoolean(String key);

    void remove(String key);

    boolean isEmpty();

    int hashCode();

    boolean equals(Object o);
}
