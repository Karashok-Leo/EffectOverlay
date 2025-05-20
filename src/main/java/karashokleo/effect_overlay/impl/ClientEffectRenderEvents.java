package karashokleo.effect_overlay.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import karashokleo.effect_overlay.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.Map;

public class ClientEffectRenderEvents
{
    @Deprecated
    private static final ArrayList<EffectRenderer> RENDERERS = new ArrayList<>();

    public static void clientTick(MinecraftClient client)
    {
        AbstractClientPlayerEntity player = client.player;
        if (player != null)
            for (Map.Entry<StatusEffect, StatusEffectInstance> entry : player.getActiveStatusEffects().entrySet())
                if (entry.getKey() instanceof FirstPlayerRenderEffect effect)
                    effect.onClientWorldRender(player, entry.getValue());
    }

    @Deprecated
    public static void worldRenderLast(WorldRenderContext context)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        WorldRenderer renderer = context.worldRenderer();
        VertexConsumerProvider.Immediate buffers = client.getBufferBuilders().getEntityVertexConsumers();
        Camera camera = client.gameRenderer.getCamera();

        RenderSystem.disableDepthTest();

        MatrixStack stack = context.matrixStack();
        stack.push();
        for (EffectRenderer icon : RENDERERS)
            icon.render(stack, buffers, context.tickDelta(), camera, renderer.entityRenderDispatcher);
        buffers.draw();
        stack.pop();

        RenderSystem.enableDepthTest();

        RENDERERS.clear();
    }

    public static void onLivingRenderEvents(LivingEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider)
    {
        if (entity.getCommandTags().contains("ClientOnly")) return;
        Map<StatusEffect, Integer> syncEffects = entity.getSyncEffects();
        if (syncEffects.isEmpty()) return;
        ArrayList<EffectRenderer> renderers = new ArrayList<>();
        for (Map.Entry<StatusEffect, Integer> entry : syncEffects.entrySet())
            if (entry.getKey() instanceof ClientRenderEffect effect)
            {
                EffectRenderer renderer = effect.getRenderer(entity, entry.getValue());
                if (renderer == null) continue;
                renderers.add(renderer);
            }

        if (!EffectRenderer.BEFORE_RENDER.invoker().beforeRender(entity, renderers))
            return;

        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        EntityRenderDispatcher dispatcher = client.getEntityRenderDispatcher();
        int n = renderers.size();
        for (int i = 0; i < n; i++)
        {
            EffectRenderer renderer = renderers.get(i);
            renderer.adjustPosition(i, n);
            renderer.render(matrixStack, vertexConsumerProvider, tickDelta, camera, dispatcher);
//            RENDERERS.add(renderer);
        }
    }
}
