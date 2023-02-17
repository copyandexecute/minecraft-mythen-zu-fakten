package net.fabricmc.example.mixin.render.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static net.minecraft.client.render.entity.EnderDragonEntityRenderer.*;

@Mixin(EnderDragonEntityRenderer.class)
public abstract class EnderDragonEntityRendererMixin extends EntityRenderer<EnderDragonEntity> {
    @Shadow @Final private EnderDragonEntityRenderer.DragonEntityModel model;

    @Shadow @Final private static RenderLayer DRAGON_DECAL;

    @Shadow @Final private static Identifier EXPLOSION_TEXTURE;

    @Shadow @Final private static RenderLayer DRAGON_CUTOUT;

    @Shadow @Final private static RenderLayer DRAGON_EYES;

    @Shadow @Final private static float HALF_SQRT_3;

    protected EnderDragonEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    public void render(EnderDragonEntity enderDragonEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float h = (float)enderDragonEntity.getSegmentProperties(7, g)[0];
        float j = (float)(enderDragonEntity.getSegmentProperties(5, g)[1] - enderDragonEntity.getSegmentProperties(10, g)[1]);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-h));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(j * 10.0F));
        matrixStack.translate(0.0F, 0.0F, 1.0F);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.translate(0.0F, -1.501F, 0.0F);
        boolean bl = enderDragonEntity.hurtTime > 0;
        this.model.animateModel(enderDragonEntity, 0.0F, 0.0F, g);
        VertexConsumer vertexConsumer3;
        if (enderDragonEntity.ticksSinceDeath > 0) {
            float k = (float)enderDragonEntity.ticksSinceDeath / 200.0F;
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityAlpha(EXPLOSION_TEXTURE));
            this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, k);
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(DRAGON_DECAL);
            this.model.render(matrixStack, vertexConsumer2, i, OverlayTexture.getUv(0.0F, bl), 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            vertexConsumer3 = vertexConsumerProvider.getBuffer(DRAGON_CUTOUT);
            this.model.render(matrixStack, vertexConsumer3, i, OverlayTexture.getUv(0.0F, bl), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        vertexConsumer3 = vertexConsumerProvider.getBuffer(DRAGON_EYES);
        this.model.render(matrixStack, vertexConsumer3, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        float l;
        float m;
        if (enderDragonEntity.ticksSinceDeath > 0) {
            l = ((float)enderDragonEntity.ticksSinceDeath + g) / 200.0F;
            m = Math.min(l > 0.8F ? (l - 0.8F) / 0.2F : 0.0F, 1.0F);
            Random random = Random.create(432L);
            VertexConsumer vertexConsumer4 = vertexConsumerProvider.getBuffer(RenderLayer.getLightning());
            matrixStack.push();
            matrixStack.translate(0.0F, -1.0F, -2.0F);

            for(int n = 0; (float)n < (l + l * l) / 2.0F * 60.0F; ++n) {
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(random.nextFloat() * 360.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(random.nextFloat() * 360.0F));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(random.nextFloat() * 360.0F + l * 90.0F));
                float o = random.nextFloat() * 20.0F + 5.0F + m * 10.0F;
                float p = random.nextFloat() * 2.0F + 1.0F + m * 2.0F;
                Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
                int q = (int)(255.0F * (1.0F - m));
                putDeathLightNegativeXTerminalVertex(vertexConsumer4, matrix4f, o, p);
                putDeathLightPositiveXTerminalVertex(vertexConsumer4, matrix4f, o, p);
                putDeathLightSourceVertex(vertexConsumer4, matrix4f, q);
                putDeathLightPositiveXTerminalVertex(vertexConsumer4, matrix4f, o, p);
                putDeathLightPositiveZTerminalVertex(vertexConsumer4, matrix4f, o, p);
                putDeathLightSourceVertex(vertexConsumer4, matrix4f, q);
                putDeathLightPositiveZTerminalVertex(vertexConsumer4, matrix4f, o, p);
                putDeathLightNegativeXTerminalVertex(vertexConsumer4, matrix4f, o, p);
            }

            matrixStack.pop();
        }

        matrixStack.pop();
        if (enderDragonEntity.connectedCrystal != null) {
            matrixStack.push();
            l = (float)(enderDragonEntity.connectedCrystal.getX() - MathHelper.lerp((double)g, enderDragonEntity.prevX, enderDragonEntity.getX()));
            m = (float)(enderDragonEntity.connectedCrystal.getY() - MathHelper.lerp((double)g, enderDragonEntity.prevY, enderDragonEntity.getY()));
            float r = (float)(enderDragonEntity.connectedCrystal.getZ() - MathHelper.lerp((double)g, enderDragonEntity.prevZ, enderDragonEntity.getZ()));
            renderCrystalBeam(l, m + EndCrystalEntityRenderer.getYOffset(enderDragonEntity.connectedCrystal, g), r, g, enderDragonEntity.age, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();
        }

        super.render(enderDragonEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void putDeathLightNegativeXTerminalVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float width) {
        buffer.vertex(matrix, -HALF_SQRT_3 * width, radius, -0.5F * width).color(255, 0, 255, 0).next();
    }

    private static void putDeathLightSourceVertex(VertexConsumer buffer, Matrix4f matrix, int alpha) {
        buffer.vertex(matrix, 0.0F, 0.0F, 0.0F).color(255, 255, 255, alpha).next();
    }

    private static void putDeathLightPositiveXTerminalVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float width) {
        buffer.vertex(matrix, HALF_SQRT_3 * width, radius, -0.5F * width).color(255, 0, 255, 0).next();
    }

    private static void putDeathLightPositiveZTerminalVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float width) {
        buffer.vertex(matrix, 0.0F, radius, width).color(255, 0, 255, 0).next();
    }
}
