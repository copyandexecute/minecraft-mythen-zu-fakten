package net.fabricmc.example.mixin.render.entity.feature;

import net.fabricmc.example.render.CustomRenderLayers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.trim.ArmorTrim;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> extends FeatureRenderer<T, M> {
    @Shadow
    @Final
    private SpriteAtlasTexture armorTrimsAtlas;

    public ArmorFeatureRendererMixin(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }

    /**
     * @author NoRiskk
     * @reason weil ichs kann
     */
    @Overwrite
    private void renderTrim(ArmorMaterial armorMaterial, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, ArmorTrim armorTrim, boolean bl, A bipedEntityModel, boolean bl2, float f, float g, float h) {
        Sprite sprite = this.armorTrimsAtlas.getSprite(bl2 ? armorTrim.getLeggingsModelId(armorMaterial) : armorTrim.getGenericModelId(armorMaterial));
        VertexConsumer vertexConsumer = sprite.getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, TexturedRenderLayers.getArmorTrims(), true, bl));
        if (armorTrim.getMaterial().value().ingredient().value() == Items.ENCHANTED_GOLDEN_APPLE) {
            int j = rainbowEffect();
            f = (float) (j >> 16 & 255) / 255.0F;
            g = (float) (j >> 8 & 255) / 255.0F;
            h = (float) (j & 255) / 255.0F;
        }
        bipedEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, f, g, h, 1F);
    }

    private static int rainbowEffect() {
        return Color.HSBtoRGB((float) (System.currentTimeMillis() % 7000L) / 7000F, 1F, 1F);
    }
}
