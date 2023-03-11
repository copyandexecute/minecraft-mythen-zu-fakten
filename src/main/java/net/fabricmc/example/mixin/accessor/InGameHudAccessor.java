package net.fabricmc.example.mixin.accessor;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Invoker("renderOverlay")
    void invokeRenderOverlay(MatrixStack matrices, Identifier texture, float opacity);
}
