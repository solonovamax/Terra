package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.CompoundTag;
import com.dfsek.terra.api.platform.nbt.ListTag;
import com.dfsek.terra.api.platform.nbt.Tag;
import com.dfsek.terra.api.util.GlueList;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

public class FabricListTag extends AbstractList<Tag> implements ListTag {
    private final net.minecraft.nbt.ListTag delegate;

    public FabricListTag(net.minecraft.nbt.ListTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.nbt.ListTag getHandle() {
        return delegate;
    }

    @Override
    public ListTag copy() {
        return new FabricListTag(delegate.copy());
    }

    @Override
    public CompoundTag getCompound(int index) {
        return new FabricCompoundTag(delegate.getCompound(index));
    }

    @Override
    public ListTag getList(int index) {
        return new FabricListTag(delegate.getList(index));
    }

    @Override
    public short getShort(int index) {
        return delegate.getShort(index);
    }

    @Override
    public int getInt(int i) {
        return delegate.getInt(i);
    }

    @Override
    public int[] getIntArray(int index) {
        return delegate.getIntArray(index);
    }

    @Override
    public double getDouble(int index) {
        return delegate.getDouble(index);
    }

    @Override
    public float getFloat(int index) {
        return delegate.getFloat(index);
    }

    @Override
    public String getString(int index) {
        return delegate.getString(index);
    }

    @Override
    public boolean setTag(int index, Tag tag) {
        return false;
    }

    @Override
    public boolean addTag(int index, Tag tag) {
        return false;
    }

    @Override
    public boolean canAdd(Tag tag) {
        return false;
    }

    @Override
    public byte getElementType() {
        return 0;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if(o instanceof Tag)
            return delegate.contains(((Tag) o).getHandle());
        else
            return delegate.contains(o);
    }

    @Override
    public boolean remove(Object o) {
        if(o instanceof Tag)
            return delegate.remove(((Tag) o).getHandle());
        else
            return delegate.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        List<Object> tempList = new GlueList<>();
        for(Object o : c) {
            if(o instanceof Tag)
                tempList.add(((Tag) o).getHandle());
            else
                tempList.add(o);
        }
        return delegate.containsAll(tempList);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Tag> c) {
        List<net.minecraft.nbt.Tag> tempList = new GlueList<>();
        for(Tag o : c) {
            tempList.add((net.minecraft.nbt.Tag) o.getHandle());
        }
        return delegate.addAll(tempList);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        List<Object> tempList = new GlueList<>();
        for(Object o : c) {
            if(o instanceof Tag)
                tempList.add(((Tag) o).getHandle());
            else
                tempList.add(o);
        }
        return delegate.removeAll(tempList);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        List<Object> tempList = new GlueList<>();
        for(Object o : c) {
            if(o instanceof Tag)
                tempList.add(((Tag) o).getHandle());
            else
                tempList.add(o);
        }
        return delegate.retainAll(tempList);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public boolean add(Tag tag) {
        return delegate.add((net.minecraft.nbt.Tag) tag.getHandle());
    }

    @Override
    public Tag get(int i) {
        return null;
    }

    @Override
    public Tag set(int index, Tag element) {
        return FabricTagHelper.getAppropriateTag(delegate.set(index, (net.minecraft.nbt.Tag) element.getHandle()));
    }

    @Override
    public void add(int i, Tag tag) {

    }

    @Override
    public Tag remove(int i) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        if(o instanceof Tag)
            return delegate.indexOf(((Tag) o).getHandle());
        else
            return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o instanceof Tag)
            return delegate.lastIndexOf(((Tag) o).getHandle());
        else
            return delegate.lastIndexOf(o);
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends Tag> c) {
        List<net.minecraft.nbt.Tag> tempList = new GlueList<>();
        for(Tag o : c) {
            tempList.add((net.minecraft.nbt.Tag) o.getHandle());
        }
        return delegate.addAll(index, tempList);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return delegate.equals(((FabricListTag) o).delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
