package com.dfsek.terra.api.platform.handle;

import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.inventory.Enchantment;
import com.dfsek.terra.api.platform.inventory.ItemStack;

import java.util.Set;

public interface ItemHandle {
    ItemStack newItemStack(MaterialData material, int amount);

    Enchantment getEnchantment(String id);

    Set<Enchantment> getEnchantments();
}
