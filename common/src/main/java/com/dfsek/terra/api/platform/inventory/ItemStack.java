package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.nbt.CompoundTag;
import com.dfsek.terra.api.platform.nbt.Tag;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public interface ItemStack extends Handle {
    int getAmount();
    
    void setAmount(int i);
    
    MaterialData getType();
    
    ItemStack copy();
    
    boolean hasTag();
    
    CompoundTag getTag();
    
    void setTag(@Nullable CompoundTag tag);
    
    CompoundTag getOrCreateTag();
    
    CompoundTag getOrCreateSubTag(String key);
    
    CompoundTag getSubTag(String key);
    
    int getDamage();
    
    void setDamage(int damage);
    
    int getMaxDamage();
    
    Map<Enchantment, Integer> getEnchantments();
    
    void removeSubTag(String key);
    
    ItemStack setCustomName(@Nullable String json);
    
    void removeCustomName();
    
    boolean hasCustomName();
    
    boolean hasGlint();
    
    boolean isEnchantable();
    
    void addEnchantment(Enchantment enchantment, int level);
    
    boolean hasEnchantments();
    
    void putSubTag(String key, Tag tag);
    
    void setRepairCost(int repairCost);
    
    default void increment(int amount) {
        this.setAmount(this.getAmount() + amount);
    }
    
    default void decrement(int amount) {
        this.increment(-amount);
    }
}
