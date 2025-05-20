package karashokleo.effect_overlay.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface IconOverlayEffect extends ClientRenderEffect
{
    @Override
    @Nullable
    default EffectRenderer getRenderer(LivingEntity entity, int lv)
    {
        return entity == MinecraftClient.getInstance().player ? null : getIcon(entity, lv);
    }

    IconEffectRenderer getIcon(LivingEntity entity, int lv);
}
