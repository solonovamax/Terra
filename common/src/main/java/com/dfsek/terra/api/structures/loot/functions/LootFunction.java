package com.dfsek.terra.api.structures.loot.functions;


import com.dfsek.terra.api.platform.inventory.ItemStack;

import java.util.Random;


/**
 * Interface for mutating items in Loot Tables.
 */
public interface LootFunction {
    /**
     * Applies the function to an ItemStack.
     *
     * @param original The ItemStack on which to apply the function.
     * @param r        The Random instance to use.
     *
     * @return - ItemStack - The mutated ItemStack.
     */
    ItemStack apply(ItemStack original, Random r);
}
