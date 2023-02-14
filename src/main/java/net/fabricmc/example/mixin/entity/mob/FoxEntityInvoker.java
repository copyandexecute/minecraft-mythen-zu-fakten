package net.fabricmc.example.mixin.entity.mob;

import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FoxEntity.class)
public interface FoxEntityInvoker {
    @Invoker("spit")
    void invokeSpit(ItemStack itemStack);
}
