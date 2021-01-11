package com.dfsek.terra.bukkit.world.inventory;

import com.dfsek.terra.api.platform.inventory.Enchantment;
import com.dfsek.terra.api.platform.inventory.ItemStack;

public class BukkitEnchantment implements Enchantment {
    private final org.bukkit.enchantments.Enchantment delegate;

    public BukkitEnchantment(org.bukkit.enchantments.Enchantment delegate) {
        this.delegate = delegate;
    }

    @Override
    public org.bukkit.enchantments.Enchantment getHandle() {
        return delegate;
    }

    @Override
    public boolean isAcceptableItem(ItemStack itemStack) {
        return delegate.canEnchantItem((org.bukkit.inventory.ItemStack) itemStack.getHandle());
    }

    @Override
    public String getName() {
        return delegate.getKey().getNamespace();
    }

    @Override
    public boolean canCombine(Enchantment other) {
        return delegate.conflictsWith((org.bukkit.enchantments.Enchantment) other.getHandle());
    }

    @Override
    public int getMinLevel() {
        return delegate.getStartLevel();
    }

    @Override
    public int getMaxLevel() {
        return delegate.getMaxLevel();
    }
}
