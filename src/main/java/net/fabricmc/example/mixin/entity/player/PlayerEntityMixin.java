package net.fabricmc.example.mixin.entity.player;

import net.fabricmc.example.entity.LeashPlayer;
import net.fabricmc.example.mixin.world.WorldAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements LeashPlayer {
    @Shadow
    public abstract ItemStack eatFood(World world, ItemStack stack);

    private final Set<UUID> entities = new HashSet<>();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovementInjection(CallbackInfo ci) {
        Vec3d vec3d = this.getVelocity();
        int attachedChickens = getAttachedChickens();
        if (attachedChickens > 0 && !this.onGround && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 1.0 / attachedChickens, 1.0));
        }
    }

    @NotNull
    @Override
    public Set<UUID> getEntities() {
        return entities;
    }

    private int getAttachedChickens() {
        return (int) entities.stream().map(uuid -> ((WorldAccessor) world).invokeGetEntityLookup().get(uuid)).filter(Objects::nonNull).count();
    }

    @Override
    public void handleUuid(@NotNull UUID uuid, boolean attached) {
        if (attached) {
            entities.add(uuid);
        } else {
            entities.remove(uuid);
        }
    }
}
