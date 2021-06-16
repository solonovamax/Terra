package com.dfsek.terra.api.platform.inventory.item;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.inventory.ItemStack;


public interface Enchantment extends Handle {
    boolean canEnchantItem(ItemStack itemStack);
    
    boolean conflictsWith(Enchantment other);
    
    String getID();
    
    int getMaxLevel();
}
