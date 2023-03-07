package net.fabricmc.example.mixin.render.entity;

import net.fabricmc.example.myth.myths.DinnerboneHeadMyth;
import net.fabricmc.example.myth.myths.DinnerboneSleepingMyth;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "shouldFlipUpsideDown", at = @At("HEAD"), cancellable = true)
    private static void dinnerboneMyth(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (DinnerboneHeadMyth.INSTANCE.test(entity)) cir.setReturnValue(true);
    }

    @Redirect(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getSleepingDirection()Lnet/minecraft/util/math/Direction;"))
    private Direction dinnerboneSleepingMyth(LivingEntity instance) {
        //TODO translaten
        return DinnerboneSleepingMyth.INSTANCE.apply(instance);
    }

    @Redirect(method = "setupTransforms", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getSleepingDirection()Lnet/minecraft/util/math/Direction;"))
    private Direction dinnerboneSleepingMyth2(LivingEntity instance) {
        return DinnerboneSleepingMyth.INSTANCE.apply(instance);
    }
}
