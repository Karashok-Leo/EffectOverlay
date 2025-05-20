package karashokleo.effect_overlay;

import karashokleo.effect_overlay.impl.ClientEffectRenderEvents;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class EffectOverlay implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        ClientTickEvents.END_CLIENT_TICK.register(ClientEffectRenderEvents::clientTick);
//        WorldRenderEvents.LAST.register(ClientEffectRenderEvents::worldRenderLast);
    }
}