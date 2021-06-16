package com.dfsek.terra.forge.inventory;

import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.api.platform.inventory.item.Enchantment;
import com.dfsek.terra.forge.world.ForgeAdapter;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;


public class ForgeEnchantment implements Enchantment {
    private final net.minecraft.enchantment.Enchantment enchantment;
    
    public ForgeEnchantment(net.minecraft.enchantment.Enchantment enchantment) {
        this.enchantment = enchantment;
    }
    
    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return enchantment.canEnchant(ForgeAdapter.adapt(itemStack));
    }
    
    @Override
    public boolean conflictsWith(Enchantment other) {
        return !enchantment.isCompatibleWith(ForgeAdapter.adapt(other));
    }
    
    @Override
    public net.minecraft.enchantment.Enchantment getHandle() {
        return enchantment;
    }
    
    @Override
    public int getMaxLevel() {
        return enchantment.getMaxLevel();
    }
    
    @Override
    public String getID() {
        return Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)).toString();
    }
}
