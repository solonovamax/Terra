package com.dfsek.terra.bukkit.world.inventory;

import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.inventory.Enchantment;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.nbt.CompoundTag;
import com.dfsek.terra.api.platform.nbt.Tag;
import com.dfsek.terra.bukkit.world.block.BukkitMaterialData;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class BukkitItemStack implements ItemStack {
    private org.bukkit.inventory.ItemStack delegate;

    public BukkitItemStack(org.bukkit.inventory.ItemStack delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getAmount() {
        return delegate.getAmount();
    }

    @Override
    public void setAmount(int i) {
        delegate.setAmount(i);
    }

    @Override
    public MaterialData getType() {
        return new BukkitMaterialData(delegate.getType());
    }

    @Override
    public ItemStack copy() {
        return new BukkitItemStack(delegate.clone());
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public boolean hasTag() {
        return false;
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public CompoundTag getTag() {
        return null;
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public void setTag(@Nullable CompoundTag tag) {
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public CompoundTag getOrCreateTag() {
        return null;
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public CompoundTag getOrCreateSubTag(String key) {
        return null;
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public CompoundTag getSubTag(String key) {
        return null;
    }

    @Override
    public int getDamage() {
        return delegate.getItemMeta() instanceof Damageable ? ((Damageable) delegate.getItemMeta()).getDamage() : 0;
    }

    @Override
    public void setDamage(int damage) {
        if(delegate.getItemMeta() instanceof Damageable) {
            ((Damageable) delegate.getItemMeta()).setDamage(damage);
        }
    }

    @Override
    public int getMaxDamage() {
        return delegate.getData() != null ? delegate.getData().getItemType().getMaxDurability() : 0;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        if(delegate.getItemMeta() != null)
            return delegate.getItemMeta()
                    .getEnchants()
                    .entrySet()
                    .stream()
                    .map(entry -> new AbstractMap.SimpleEntry<>(new BukkitEnchantment(entry.getKey()), entry.getValue()))
                    .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
        else
            return new HashMap<>();
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public void removeSubTag(String key) {

    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public ItemStack setCustomName(@Nullable String json) {
//        if(delegate.getItemMeta() != null) {
//            delegate.getItemMeta().setDisplayName(json);
//        }
        return this;
    }

    @Override
    public void removeCustomName() {
        if(delegate.getItemMeta() != null)
            delegate.getItemMeta().setDisplayName(null);
    }

    @Override
    public boolean hasCustomName() {
        return delegate.getItemMeta() != null && delegate.getItemMeta().hasDisplayName();
    }

    @Override
    public boolean hasGlint() {
        return delegate.getItemMeta() != null && delegate.getItemMeta().hasEnchants();
    }

    @Override
    public boolean isEnchantable() {
        for(org.bukkit.enchantments.Enchantment ench : org.bukkit.enchantments.Enchantment.values())
            if(!ench.canEnchantItem(delegate))
                return false;
        return true;
    }

    @Override
    public void addEnchantment(Enchantment enchantment, int level) {
        if(delegate.getItemMeta() != null)
            delegate.getItemMeta().addEnchant((org.bukkit.enchantments.Enchantment) enchantment.getHandle(), level, true);
    }

    @Override
    public boolean hasEnchantments() {
        return delegate.getItemMeta() != null && delegate.getItemMeta().hasEnchants();
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public void putSubTag(String key, Tag tag) {
    }

    /**
     * No implementation (we don't want to use NMS in the base plugin.
     * But, there will be an addon that allows for this, eventually.)
     */
    @Override
    public void setRepairCost(int repairCost) {
    }

    @Override
    public ItemStack clone() {
        BukkitItemStack clone;
        try {
            clone = (BukkitItemStack) super.clone();
            clone.delegate = delegate.clone();
        } catch(CloneNotSupportedException e) {
            throw new Error(e);
        }
        return clone;
    }

    @Override
    public org.bukkit.inventory.ItemStack getHandle() {
        return delegate;
    }
}
