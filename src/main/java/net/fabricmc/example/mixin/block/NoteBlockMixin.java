package net.fabricmc.example.mixin.block;

import net.fabricmc.example.block.NoteBlockModifier;
import net.fabricmc.example.sound.SoundManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoteBlock.class)
public abstract class NoteBlockMixin {
    @Shadow
    @Final
    public static EnumProperty<Instrument> INSTRUMENT;

    @Shadow
    @Nullable
    protected abstract Identifier getCustomSound(World world, BlockPos pos);

    @Inject(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FFJ)V", shift = At.Shift.BEFORE))
    private void onSyncedBlockEventInjection(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        Instrument instrument = state.get(INSTRUMENT);
        if (instrument == Instrument.ZOMBIE) {
            for (VillagerEntity villager : world.getEntitiesByClass(VillagerEntity.class, Box.of(pos.toCenterPos(), 25, 10, 25), villagerEntity -> true)) {
                villager.getBrain().doExclusively(Activity.PANIC);
            }
        }
    }

    @Inject(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/NoteBlock;getCustomSound(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/Identifier;", shift = At.Shift.BEFORE))
    private void onSyncedBlockEventInjection2(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
        Identifier customSound = this.getCustomSound(world, pos);
        if (SoundManager.INSTANCE.getHUGO_BOMB_ID().equals(customSound)) {
            NoteBlockModifier.INSTANCE.explode(world, pos);
        }
    }

    @Redirect(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/enums/Instrument;hasCustomSound()Z"))
    private boolean onSyncedBlockEventInjection2(Instrument instance) {
        return instance.hasCustomSound() || "CUSTOM_HEAD".equals(instance.toString());
    }
}
