package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;
import com.dfsek.terra.api.platform.inventory.item.Damageable;
import com.dfsek.terra.api.platform.inventory.item.ItemMeta;


public interface ItemStack extends Handle {
    int getAmount();
    
    void setAmount(int i);
    
    ItemMeta getItemMeta();
    
    void setItemMeta(ItemMeta meta);
    
    Item getType();
    
    default boolean isDamageable() {
        return getItemMeta() instanceof Damageable;
    }
}
