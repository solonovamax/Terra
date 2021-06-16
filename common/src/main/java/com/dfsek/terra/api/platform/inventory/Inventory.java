package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;


public interface Inventory extends Handle {
    void setItem(int slot, ItemStack newStack);
    
    ItemStack getItem(int slot);
    
    int getSize();
}
