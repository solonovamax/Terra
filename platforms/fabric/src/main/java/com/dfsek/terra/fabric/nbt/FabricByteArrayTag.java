package com.dfsek.terra.fabric.nbt;

import com.dfsek.terra.api.platform.nbt.ByteArrayTag;
import com.dfsek.terra.api.platform.nbt.ByteTag;
import com.dfsek.terra.api.platform.nbt.Tag;
import com.dfsek.terra.api.util.GlueList;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;

public class FabricByteArrayTag extends AbstractList<ByteTag> implements ByteArrayTag {
    private final net.minecraft.nbt.ByteArrayTag delegate;

    public FabricByteArrayTag(net.minecraft.nbt.ByteArrayTag delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.nbt.ByteArrayTag getHandle() {
        return delegate;
    }

    @Override
    public byte[] getByteArray() {
        return delegate.getByteArray();
    }

    @Override
    public boolean setTag(int index, Tag tag) {
        return delegate.setTag(index, (net.minecraft.nbt.Tag) tag.getHandle());
    }

    @Override
    public boolean addTag(int index, Tag tag) {
        return delegate.addTag(index, (net.minecraft.nbt.Tag) tag.getHandle());
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
    public boolean addAll(@NotNull Collection<? extends ByteTag> c) {
        List<net.minecraft.nbt.ByteTag> tempList = new GlueList<>();
        for(ByteTag o : c) {
            tempList.add((net.minecraft.nbt.ByteTag) o.getHandle());
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
    public boolean add(ByteTag tag) {
        return delegate.add((net.minecraft.nbt.ByteTag) tag.getHandle());
    }

    @Override
    public ByteTag get(int i) {
        return new FabricByteTag(delegate.get(i));
    }

    @Override
    public ByteTag set(int i, ByteTag byteTag) {
        return new FabricByteTag(delegate.set(i, (net.minecraft.nbt.ByteTag) byteTag.getHandle()));
    }

    @Override
    public void add(int i, ByteTag byteTag) {
        delegate.add(i, (net.minecraft.nbt.ByteTag) byteTag.getHandle());
    }

    @Override
    public ByteTag remove(int i) {
        net.minecraft.nbt.ByteTag delegateTag = delegate.remove(i);
        return new FabricByteTag(delegateTag);
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
    public boolean addAll(int index, @NotNull Collection<? extends ByteTag> c) {
        List<net.minecraft.nbt.ByteTag> tempList = new GlueList<>();
        for(ByteTag o : c) {
            tempList.add((net.minecraft.nbt.ByteTag) o.getHandle());
        }
        return delegate.addAll(index, tempList);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        return delegate.equals(((FabricByteArrayTag) o).delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public FabricByteArrayTag copy() {
        return new FabricByteArrayTag((net.minecraft.nbt.ByteArrayTag) delegate.copy());
    }
}
