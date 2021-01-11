package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.CompoundTag;
import com.dfsek.terra.api.platform.nbt.ListTag;
import com.dfsek.terra.api.platform.nbt.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FabricCompoundTag implements CompoundTag {
    private final net.minecraft.nbt.CompoundTag delegate;

    public FabricCompoundTag(net.minecraft.nbt.CompoundTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.nbt.CompoundTag getHandle() {
        return delegate;
    }

    @Override
    public CompoundTag copy() {
        return new FabricCompoundTag(delegate.copy());
    }

    @Override
    public Set<String> getKeys() {
        return delegate.getKeys();
    }

    @Override
    public int getSize() {
        return delegate.getSize();
    }

    @Override
    public Tag put(String key, Tag tag) {
        return FabricTagHelper.getAppropriateTag(delegate.put(key, (net.minecraft.nbt.Tag) tag.getHandle()));
    }

    @Override
    public void putByte(String key, byte value) {
        delegate.putByte(key, value);
    }

    @Override
    public void putShort(String key, short value) {
        delegate.putShort(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        delegate.putInt(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        delegate.putLong(key, value);
    }

    @Override
    public void putUuid(String key, UUID value) {
        delegate.putUuid(key, value);
    }

    @Override
    public UUID getUuid(String key) {
        return delegate.getUuid(key);
    }

    @Override
    public boolean containsUuid(String key) {
        return delegate.containsUuid(key);
    }

    @Override
    public void putFloat(String key, float value) {
        delegate.putFloat(key, value);
    }

    @Override
    public void putDouble(String key, double value) {
        delegate.putDouble(key, value);
    }

    @Override
    public void putString(String key, String value) {
        delegate.putString(key, value);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        delegate.putByteArray(key, value);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        delegate.putIntArray(key, value);
    }

    @Override
    public void putIntArray(String key, List<Integer> value) {
        delegate.putIntArray(key, value);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        delegate.putLongArray(key, value);
    }

    @Override
    public void putLongArray(String key, List<Long> value) {
        delegate.putLongArray(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        delegate.putBoolean(key, value);
    }

    @Override
    public Tag get(String key) {
        return FabricTagHelper.getAppropriateTag(delegate.get(key));
    }

    @Override
    public byte getType(String key) {
        return delegate.getType(key);
    }

    @Override
    public boolean contains(String key) {
        return delegate.contains(key);
    }

    @Override
    public boolean contains(String key, int type) {
        return delegate.contains(key, type);
    }

    @Override
    public byte getByte(String key) {
        return delegate.getByte(key);
    }

    @Override
    public short getShort(String key) {
        return delegate.getShort(key);
    }

    @Override
    public int getInt(String key) {
        return delegate.getInt(key);
    }

    @Override
    public long getLong(String key) {
        return delegate.getLong(key);
    }

    @Override
    public float getFloat(String key) {
        return delegate.getFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return delegate.getDouble(key);
    }

    @Override
    public String getString(String key) {
        return delegate.getString(key);
    }

    @Override
    public byte[] getByteArray(String key) {
        return delegate.getByteArray(key);
    }

    @Override
    public int[] getIntArray(String key) {
        return delegate.getIntArray(key);
    }

    @Override
    public long[] getLongArray(String key) {
        return delegate.getLongArray(key);
    }

    @Override
    public CompoundTag getCompound(String key) {
        return new FabricCompoundTag(delegate.getCompound(key));
    }

    @Override
    public ListTag getList(String key, int type) {
        return new FabricListTag(delegate.getList(key, type));
    }

    @Override
    public boolean getBoolean(String key) {
        return delegate.getBoolean(key);
    }

    @Override
    public void remove(String key) {
        delegate.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return delegate.equals(((FabricCompoundTag) o).delegate);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
