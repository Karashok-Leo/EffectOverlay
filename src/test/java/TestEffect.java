import karashokleo.effect_overlay.api.IconEffectRenderer;
import karashokleo.effect_overlay.api.IconOverlayEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class TestEffect extends StatusEffect implements IconOverlayEffect
{
    protected TestEffect()
    {
        super(StatusEffectCategory.HARMFUL, 0xFF0000);
    }

    @Override
    public IconEffectRenderer getIcon(LivingEntity entity, int lv)
    {
        return IconEffectRenderer.icon(entity, new Identifier(TestMod.MOD_ID,"textures/flame.png"));
    }
}
