package karashokleo.effect_overlay.mixin;

import karashokleo.effect_overlay.api.SyncEffectsProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("all")
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SyncEffectsProvider
{
    @Shadow
    @Final
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;

    @Unique
    private static final TrackedData<String> SYNC_EFFECTS = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.STRING);

    @Unique
    public final Map<StatusEffect, Integer> sync_effects = new TreeMap<>(Comparator.comparing(StatusEffect::getTranslationKey));

    public LivingEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(
        method = "initDataTracker",
        at = @At("TAIL")
    )
    private void inject_initDataTracker(CallbackInfo info)
    {
        this.dataTracker.startTracking(SYNC_EFFECTS, "");
    }

    @Inject(
        method = "updatePotionVisibility",
        at = @At("HEAD")
    )
    private void inject_updatePotionVisibility(CallbackInfo ci)
    {
        if (activeStatusEffects.isEmpty())
        {
            this.dataTracker.set(SYNC_EFFECTS, "");
        } else
        {
            this.dataTracker.set(SYNC_EFFECTS, activeStatusEffects.isEmpty() ? "" : SyncEffectsProvider.writeToString(activeStatusEffects));
        }
    }

    @Inject(
        method = "tickStatusEffects",
        at = @At("TAIL")
    )
    private void inject_tickStatusEffects(CallbackInfo info)
    {
        sync_effects.clear();
        sync_effects.putAll(SyncEffectsProvider.readFromString(this.dataTracker.get(SYNC_EFFECTS)));
    }

    @Override
    public Map<StatusEffect, Integer> getSyncEffects()
    {
        return sync_effects;
    }
}