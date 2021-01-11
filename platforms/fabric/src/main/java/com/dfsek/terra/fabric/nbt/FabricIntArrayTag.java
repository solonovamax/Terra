package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.IntArrayTag;
import com.dfsek.terra.api.platform.nbt.IntTag;
import com.dfsek.terra.api.platform.nbt.Tag;
import com.dfsek.terra.api.util.GlueList;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

public class FabricIntArrayTag extends AbstractList<IntTag> implements IntArrayTag {
    private final net.minecraft.nbt.IntArrayTag delegate;

    public FabricIntArrayTag(net.minecraft.nbt.IntArrayTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.nbt.IntArrayTag getHandle() {
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
    public boolean addAll(@NotNull Collection<? extends IntTag> c) {
        List<net.minecraft.nbt.IntTag> tempList = new GlueList<>();
        for(IntTag o : c) {
            tempList.add((net.minecraft.nbt.IntTag) o.getHandle());
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
    public boolean add(IntTag tag) {
        return delegate.add((net.minecraft.nbt.IntTag) tag.getHandle());
    }

    @Override
    public IntTag get(int i) {
        return new FabricIntTag(delegate.get(i));
    }

    @Override
    public IntTag set(int i, IntTag byteTag) {
        return new FabricIntTag(delegate.set(i, (net.minecraft.nbt.IntTag) byteTag.getHandle()));
    }

    @Override
    public void add(int i, IntTag byteTag) {
        delegate.add(i, (net.minecraft.nbt.IntTag) byteTag.getHandle());
    }

    @Override
    public IntTag remove(int i) {
        net.minecraft.nbt.IntTag delegateTag = delegate.remove(i);
        return new FabricIntTag(delegateTag);
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
    public boolean addAll(int index, @NotNull Collection<? extends IntTag> c) {
        List<net.minecraft.nbt.IntTag> tempList = new GlueList<>();
        for(IntTag o : c) {
            tempList.add((net.minecraft.nbt.IntTag) o.getHandle());
        }
        return delegate.addAll(index, tempList);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return delegate.equals(((FabricIntArrayTag) o).delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public FabricIntArrayTag copy() {
        return new FabricIntArrayTag(delegate.copy());
    }

    @Override
    public int[] getIntArray() {
        return delegate.getIntArray();
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
