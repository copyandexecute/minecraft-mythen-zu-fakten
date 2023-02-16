package net.fabricmc.example.mixin.block.entity;

import com.mojang.authlib.GameProfile;
import net.fabricmc.example.sound.SoundManager;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SkullBlockEntity.class)
public abstract class SkullBlockEntityMixin {

    @Shadow
    private @Nullable Identifier noteBlockSound;

    @Shadow
    private @Nullable GameProfile owner;

    /**
     * @author NoRiskk
     * @reason weil ichs kann
     */
    @Overwrite
    public @Nullable Identifier getNoteBlockSound() {
        if (this.noteBlockSound != null) return this.noteBlockSound;
        if (owner == null) return null;
        return switch (owner.getId().toString()) {
            case "f6f3a530-6c39-4098-96a0-6bdf4f3afc70" -> SoundManager.INSTANCE.getAYYY_ZIP_ID(); //ghg
            case "046691d6-a0ae-452d-9afd-114900493469" -> SoundManager.INSTANCE.getHUGO_BOMB_ID(); //hugo
            case "6201fc85-29b3-467a-acbd-320731a4ca39" -> SoundManager.INSTANCE.getTRYMACS_MOIN_ID(); //trymacs
            case "0508c8d8-3cba-46c3-9275-db20d2d99ceb" -> SoundManager.INSTANCE.getWICHTIGER_IWAS_ID(); //wichtiger
            case "924dc6fc-eb8b-4af0-bea4-be4fecd7914d" -> SoundManager.INSTANCE.getMONTE_VERPISS_DICH_ID(); //monte
            default -> null;
        };
    }
}
