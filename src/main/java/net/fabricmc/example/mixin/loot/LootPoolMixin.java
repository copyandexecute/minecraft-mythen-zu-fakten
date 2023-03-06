package net.fabricmc.example.mixin.loot;

import net.fabricmc.example.myth.myths.LuckMyth;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.LootNumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LootPool.class)
public abstract class LootPoolMixin {
    @Redirect(method = "addGeneratedLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/provider/number/LootNumberProvider;nextFloat(Lnet/minecraft/loot/context/LootContext;)F"))
    private float luckMyth(LootNumberProvider instance, LootContext lootContext) {
        return LuckMyth.INSTANCE.apply(instance, lootContext);
    }
}
