package com.dfsek.terra.api.platform.inventory;

import com.dfsek.terra.api.platform.Handle;

public interface Enchantment extends Handle {
    boolean isAcceptableItem(ItemStack itemStack);

    String getName();

    boolean canCombine(Enchantment other);

    int getMinLevel();

    int getMaxLevel();
}
