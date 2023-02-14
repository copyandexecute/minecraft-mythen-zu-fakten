package net.fabricmc.example.render.entity.model

import net.minecraft.client.model.Model
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Identifier
import java.util.function.Function

open class CosmeticModelBase(layerFactory: Function<Identifier, RenderLayer>) : Model(layerFactory) {
    var limbAngle = 0f
    var limbDistance = 0f
    var tickDelta = 0f
    var animationProgress = 0f
    var headYaw = 0f
    var headPitch = 0f

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
    }
}
