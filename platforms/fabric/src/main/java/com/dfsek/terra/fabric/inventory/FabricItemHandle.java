package com.dfsek.terra.fabric.inventory;

import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.ItemHandle;
import com.dfsek.terra.api.platform.inventory.Enchantment;
import com.dfsek.terra.api.platform.inventory.ItemStack;
import com.dfsek.terra.fabric.inventory.item.FabricEnchantment;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FabricItemHandle implements ItemHandle {
    @Override
    public ItemStack newItemStack(MaterialData material, int amount) {
        return new FabricItemStack(new net.minecraft.item.ItemStack((ItemConvertible) material.getHandle(), amount));
    }

    @Override
    public Enchantment getEnchantment(String id) {
        return new FabricEnchantment(Registry.ENCHANTMENT.get(new Identifier(id)));
    }

    @Override
    public Set<Enchantment> getEnchantments() {
        return Registry.ENCHANTMENT.getEntries()
                .stream()
                .map(Map.Entry::getValue)
                .map(FabricEnchantment::new)
                .collect(Collectors.toSet());
    }
}
