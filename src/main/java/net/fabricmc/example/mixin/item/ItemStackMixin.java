package net.fabricmc.example.mixin.item;

import net.fabricmc.example.events.ItemBreakCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;", shift = At.Shift.AFTER))
    private <T extends LivingEntity> void injected(int i, T livingEntity, Consumer<T> consumer, CallbackInfo ci) {
        ItemBreakCallback.EVENT.invoker().onItemBreak(livingEntity, (ItemStack) (Object) this);
    }
}
