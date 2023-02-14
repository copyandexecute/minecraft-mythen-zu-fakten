package net.fabricmc.example.render.entity.model

import java.util.function.Function
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.model.*
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper

class AllayWingsModel :
    CosmeticModelBase({ RenderLayer.getEntityCutout(ALLAY_WINGS_TEXTURE) }) {
    private val leftWing: ModelPart
    private val rightWing: ModelPart

    companion object {
        val ALLAY_WINGS_TEXTURE = Identifier("modid", "textures/cosmetics/allay_wings.png")
    }

    init {
        val model = getTexturedModelData().createModel()
        leftWing = model.getChild(EntityModelPartNames.LEFT_WING)
        rightWing = model.getChild(EntityModelPartNames.RIGHT_WING)
    }

    private fun getTexturedModelData(): TexturedModelData {
        val modelData = ModelData()
        val modelPartData = modelData.root
        modelPartData.addChild(
            EntityModelPartNames.LEFT_WING,
            ModelPartBuilder.create().uv(16, 14).mirrored().cuboid(0.0f, 0.0f, 0.0f, 0.0f, 5.0f, 8.0f, Dilation(0.0f))
                .mirrored(false),
            ModelTransform.pivot(0.5f, 1.0f, 1.0f)
        )
        modelPartData.addChild(
            EntityModelPartNames.RIGHT_WING,
            ModelPartBuilder.create().uv(16, 14).cuboid(0.0f, 0.0f, 0.0f, 0.0f, 5.0f, 8.0f, Dilation(0.0f)),
            ModelTransform.pivot(-0.5f, 1.0f, 1.0f)
        )
        return TexturedModelData.of(modelData, 32, 32)
    }

    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float,
    ) {
        rightWing.pivotZ = 2.0f
        leftWing.pivotZ = 2.0f
        rightWing.pivotY = 1.0f
        leftWing.pivotY = 1.0f
        rightWing.yaw = 0.47123894f + MathHelper.cos(animationProgress * 0.4f) * 3.1415927f * 0.05f
        leftWing.yaw = -rightWing.yaw
        leftWing.roll = -0.47123894f
        leftWing.pitch = 0.47123894f
        rightWing.pitch = 0.47123894f
        rightWing.roll = 0.47123894f
        leftWing.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        rightWing.render(matrices, vertices, light, overlay, red, green, blue, alpha)
    }
}
