package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.LongArrayTag;
import com.dfsek.terra.api.platform.nbt.LongTag;
import com.dfsek.terra.api.platform.nbt.Tag;
import com.dfsek.terra.api.util.GlueList;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

public class FabricLongArrayTag extends AbstractList<LongTag> implements LongArrayTag {
    private final net.minecraft.nbt.LongArrayTag delegate;

    public FabricLongArrayTag(net.minecraft.nbt.LongArrayTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.nbt.LongArrayTag getHandle() {
        return delegate;
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
    public boolean addAll(@NotNull Collection<? extends LongTag> c) {
        List<net.minecraft.nbt.LongTag> tempList = new GlueList<>();
        for(LongTag o : c) {
            tempList.add((net.minecraft.nbt.LongTag) o.getHandle());
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
    public boolean add(LongTag tag) {
        return delegate.add((net.minecraft.nbt.LongTag) tag.getHandle());
    }

    @Override
    public LongTag get(int i) {
        return new FabricLongTag(delegate.get(i));
    }

    @Override
    public LongTag set(int i, LongTag byteTag) {
        return new FabricLongTag(delegate.set(i, (net.minecraft.nbt.LongTag) byteTag.getHandle()));
    }

    @Override
    public void add(int i, LongTag byteTag) {
        delegate.add(i, (net.minecraft.nbt.LongTag) byteTag.getHandle());
    }

    @Override
    public LongTag remove(int i) {
        net.minecraft.nbt.LongTag delegateTag = delegate.remove(i);
        return new FabricLongTag(delegateTag);
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
        delegate.clear();
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends LongTag> c) {
        List<net.minecraft.nbt.LongTag> tempList = new GlueList<>();
        for(LongTag o : c) {
            tempList.add((net.minecraft.nbt.LongTag) o.getHandle());
        }
        return delegate.addAll(index, tempList);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return delegate.equals(((FabricLongArrayTag) o).delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public FabricLongArrayTag copy() {
        return new FabricLongArrayTag(delegate.copy());
    }

    @Override
    public long[] getLongArray() {
        return delegate.getLongArray();
    }

    @Override
    public boolean setTag(int index, Tag tag) {
        return delegate.setTag(index, (net.minecraft.nbt.Tag) tag.getHandle());
    }

    @Override
    public boolean addTag(int index, Tag tag) {
        return delegate.addTag(index, (net.minecraft.nbt.Tag) tag.getHandle());
    }
}
