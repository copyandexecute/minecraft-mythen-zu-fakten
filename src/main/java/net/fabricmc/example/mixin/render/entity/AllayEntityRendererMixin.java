package net.fabricmc.example.mixin.render.entity;

import net.fabricmc.example.entity.InvertedAllay;
import net.minecraft.client.render.entity.AllayEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.AllayEntityModel;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AllayEntityRenderer.class)
public abstract class AllayEntityRendererMixin extends MobEntityRenderer<AllayEntity, AllayEntityModel> {
    @Shadow
    @Final
    private static Identifier TEXTURE;
    private static final Identifier INVERTED_TEXTURE = new Identifier("modid", "textures/entity/allay/inverted_allay.png");

    public AllayEntityRendererMixin(EntityRendererFactory.Context context, AllayEntityModel entityModel, float f) {
        super(context, entityModel, f);
    }

    public Identifier getTexture(AllayEntity allayEntity) {
        return (((InvertedAllay) allayEntity).isInverted()) ? INVERTED_TEXTURE : TEXTURE;
    }
}
