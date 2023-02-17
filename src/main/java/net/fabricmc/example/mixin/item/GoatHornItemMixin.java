package net.fabricmc.example.mixin.item;

import net.fabricmc.example.events.GoatHornCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Instrument;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GoatHornItem.class)
public abstract class GoatHornItemMixin {
    @Inject(method = "playSound", at = @At("TAIL"))
    private static void playSoundInjection(World world, PlayerEntity player, Instrument instrument, CallbackInfo ci) {
        GoatHornCallback.EVENT.invoker().onGoating(world,player,instrument);
    }
}

