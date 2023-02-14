package net.fabricmc.example.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.joml.Matrix4f;

import static net.minecraft.client.render.RenderPhase.ARMOR_GLINT_PROGRAM;
import static net.minecraft.client.render.RenderPhase.COLOR_MASK;
import static net.minecraft.client.render.RenderPhase.DISABLE_CULLING;
import static net.minecraft.client.render.RenderPhase.EQUAL_DEPTH_TEST;
import static net.minecraft.client.render.RenderPhase.GLINT_TEXTURING;
import static net.minecraft.client.render.RenderPhase.GLINT_TRANSPARENCY;
import static net.minecraft.client.render.RenderPhase.VIEW_OFFSET_Z_LAYERING;

public class CustomRenderLayers {
    protected static final RenderPhase.Texturing RAINBOW_TEXTURING = new RenderPhase.Texturing("entity_glint_texturing", () -> {
        setupRainbowTexturing(1.2F, 4L);
    }, RenderSystem::resetTextureMatrix);

    public static final RenderLayer ARMOR_GLINT_RGB = RenderLayer.of("armor_glint_rgb", VertexFormats.POSITION_TEXTURE,
            VertexFormat.DrawMode.QUADS, 256,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(ARMOR_GLINT_PROGRAM)
                    .texture(new RenderPhase.Texture(new Identifier("modid:textures/entity/rainbow_jelly_overlays/glint_rainbow.png"), true, false))
                    .writeMaskState(COLOR_MASK)
                    .cull(DISABLE_CULLING)
                    .depthTest(EQUAL_DEPTH_TEST)
                    .transparency(GLINT_TRANSPARENCY)
                    .texturing(GLINT_TEXTURING)
                    .layering(VIEW_OFFSET_Z_LAYERING)
                    .build(false));


    private static void setupRainbowTexturing(float in, long time) {
        long i = Util.getMeasuringTimeMs() * time;
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(0, f1, 0.0F);
        matrix4f.scale(in);
        RenderSystem.setTextureMatrix(matrix4f);
    }
}
