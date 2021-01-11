package com.dfsek.terra.fabric.inventory.item;

import com.dfsek.terra.api.platform.inventory.Enchantment;
import com.dfsek.terra.api.platform.inventory.ItemStack;

public class FabricEnchantment implements Enchantment {
    private final net.minecraft.enchantment.Enchantment delegate;

    public FabricEnchantment(net.minecraft.enchantment.Enchantment delegate) {
        this.delegate = delegate;
    }

    @Override
    public net.minecraft.enchantment.Enchantment getHandle() {
        return delegate;
    }

    @Override
    public boolean isAcceptableItem(ItemStack itemStack) {
        return delegate.isAcceptableItem((net.minecraft.item.ItemStack) itemStack.getHandle());
    }

    @Override
    public String getName() {
        return delegate.getName(0).asString();
    }

    @Override
    public boolean canCombine(Enchantment other) {
        return delegate.canCombine((net.minecraft.enchantment.Enchantment) other.getHandle());
    }

    @Override
    public int getMinLevel() {
        return delegate.getMinLevel();
    }

    @Override
    public int getMaxLevel() {
        return delegate.getMaxLevel();
    }
}
