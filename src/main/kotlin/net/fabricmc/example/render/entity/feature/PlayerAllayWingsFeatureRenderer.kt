package net.fabricmc.example.render.entity.feature

import net.fabricmc.example.render.entity.model.AllayWingsModel
import net.minecraft.client.network.AbstractClientPlayerEntity
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack

class PlayerAllayWingsFeatureRenderer(context: FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) :
    FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>(context) {
    private val vexWingsModel = AllayWingsModel()
    override fun render(
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        player: AbstractClientPlayerEntity,
        limbAngle: Float,
        limbDistance: Float,
        tickDelta: Float,
        animationProgress: Float,
        headYaw: Float,
        headPitch: Float,
    ) {
        matrices.translate(0.0, 0.125, 0.0)
        if (player.isSneaking) {
            //translated(0.0, 0.125, 0.0)
        }
        val vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(AllayWingsModel.ALLAY_WINGS_TEXTURE))
        vexWingsModel.animationProgress = animationProgress
        vexWingsModel.headPitch = headPitch
        vexWingsModel.headYaw = headYaw
        vexWingsModel.limbAngle = limbAngle
        vexWingsModel.limbDistance = limbDistance
        vexWingsModel.tickDelta = tickDelta
        vexWingsModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f)
    }
}
