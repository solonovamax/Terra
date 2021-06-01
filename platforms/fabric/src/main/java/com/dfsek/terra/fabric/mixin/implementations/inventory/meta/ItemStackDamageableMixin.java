package com.dfsek.terra.fabric.mixin.implementations.inventory.meta;

import com.dfsek.terra.api.platform.inventory.item.Damageable;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;


@Mixin(ItemStack.class)
@Implements(@Interface(iface = Damageable.class, prefix = "terra$", remap = Interface.Remap.NONE))
public abstract class ItemStackDamageableMixin {
    @Intrinsic
    public int terra$getDamage() {
        return getDamage();
    }
    
    @Intrinsic
    public void terra$setDamage(int damage) {
        setDamage(damage);
    }
    
    public boolean terra$hasDamage() {
        return isDamaged();
    }
    
    @Shadow
    public abstract int getDamage();
    
    @Shadow
    public abstract void setDamage(int damage);
    
    @Shadow
    public abstract boolean isDamaged();
}
