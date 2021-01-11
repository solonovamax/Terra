package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.inventory.Enchantment;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.nbt.CompoundTag;
import com.dfsek.terra.api.platform.nbt.Tag;
import com.dfsek.terra.fabric.inventory.item.FabricEnchantment;
import com.dfsek.terra.fabric.nbt.FabricCompoundTag;
import com.dfsek.terra.fabric.nbt.FabricListTag;
import com.dfsek.terra.fabric.world.block.FabricMaterialData;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public class FabricItemStack implements ItemStack {
    private final net.minecraft.item.ItemStack delegate;

    public FabricItemStack(net.minecraft.item.ItemStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getAmount() {
        return delegate.getCount();
    }

    @Override
    public void setAmount(int i) {
        delegate.setCount(i);
    }

    @Override
    public MaterialData getType() {
        return new FabricMaterialData(null);
    }

    @Override
    public ItemStack copy() {
        return new FabricItemStack(delegate.copy());
    }

    @Override
    public boolean hasTag() {
        return delegate.hasTag();
    }

    @Override
    public CompoundTag getTag() {
        return new FabricCompoundTag(delegate.getTag());
    }

    @Override
    public void setTag(@Nullable CompoundTag tag) {
        delegate.setTag((net.minecraft.nbt.CompoundTag) (tag != null ? tag.getHandle() : null));
    }

    @Override
    public CompoundTag getOrCreateTag() {
        return new FabricCompoundTag(delegate.getOrCreateTag());
    }

    @Override
    public CompoundTag getOrCreateSubTag(String key) {
        return new FabricCompoundTag(delegate.getOrCreateSubTag(key));
    }

    @Override
    public CompoundTag getSubTag(String key) {
        return new FabricCompoundTag(delegate.getSubTag(key));
    }

    @Override
    public int getDamage() {
        return delegate.getDamage();
    }

    @Override
    public void setDamage(int damage) {
        delegate.setDamage(damage);
    }

    @Override
    public int getMaxDamage() {
        return delegate.getMaxDamage();
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        FabricListTag listTag = new FabricListTag(delegate.getEnchantments());
        Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
        for(Tag tag : listTag) {
            if(tag instanceof net.minecraft.nbt.CompoundTag) {
                net.minecraft.nbt.CompoundTag compoundTag = (net.minecraft.nbt.CompoundTag) tag;
                enchantmentMap.put(new FabricEnchantment(Registry.ENCHANTMENT.get(new Identifier(compoundTag.getString("id")))), compoundTag.getInt("lvl"));
            }
        }
        return enchantmentMap;
    }

    @Override
    public void removeSubTag(String key) {
        delegate.removeSubTag(key);
    }

    @Override
    public ItemStack setCustomName(@Nullable String json) {
        delegate.setCustomName(json != null ? Text.Serializer.fromJson(json) : null);
        return this;
    }

    @Override
    public void removeCustomName() {
        delegate.removeCustomName();
    }

    @Override
    public boolean hasCustomName() {
        return delegate.hasCustomName();
    }

    @Override
    public boolean hasGlint() {
        return delegate.hasGlint();
    }

    @Override
    public boolean isEnchantable() {
        return delegate.isEnchantable();
    }

    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        delegate.addEnchantment((net.minecraft.enchantment.Enchantment) enchantment.getHandle(), level);
    }

    @Override
    public boolean hasEnchantments() {
        return delegate.hasEnchantments();
    }

    @Override
    public void putSubTag(String key, Tag tag) {
        delegate.putSubTag(key, (net.minecraft.nbt.Tag) tag.getHandle());
    }

    @Override
    public void setRepairCost(int repairCost) {
        delegate.setRepairCost(repairCost);
    }

    @Override
    public net.minecraft.item.ItemStack getHandle() {
        return delegate;
    }
}
