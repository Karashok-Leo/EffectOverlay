package karashokleo.effect_overlay.api;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public interface FirstPlayerRenderEffect
{
    void onClientWorldRender(AbstractClientPlayerEntity player, StatusEffectInstance value);
}
