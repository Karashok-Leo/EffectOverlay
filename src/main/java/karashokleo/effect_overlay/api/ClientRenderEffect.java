package karashokleo.effect_overlay.api;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface ClientRenderEffect extends SyncEffect
{
    @Nullable
    EffectRenderer getRenderer(LivingEntity entity, int lv);
}
