package net.fabricmc.example.mixin.entity.mob;

import net.fabricmc.example.entity.LeashPlayer;
import net.fabricmc.example.entity.LeashPlayerModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.silkmc.silk.network.packet.*;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow
    private @Nullable Entity holdingEntity;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attachLeash", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerChunkManager;sendToOtherNearbyPlayers(Lnet/minecraft/entity/Entity;Lnet/minecraft/network/packet/Packet;)V", shift = At.Shift.BEFORE))
    private void attachLeashInjection(Entity entity, boolean sendPacket, CallbackInfo ci) {
        if (entity instanceof ServerPlayerEntity player) {
            LeashPlayerModifier.INSTANCE.handleLeash(player, this.getUuid(), true);
        }
    }

    @Inject(method = "detachLeash", at = @At("HEAD"))
    private void detachLeashInjection(CallbackInfo ci) {
        if (this.holdingEntity instanceof ServerPlayerEntity player && !this.world.isClient()) {
            LeashPlayerModifier.INSTANCE.handleLeash(player, this.getUuid(), false);
        }
    }
}
