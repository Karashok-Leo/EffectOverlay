package karashokleo.effect_overlay.api;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public interface SyncEffectsProvider
{
    static Map<StatusEffect, Integer> readFromString(String string)
    {
        Map<StatusEffect, Integer> map = new TreeMap<>(Comparator.comparing(StatusEffect::getTranslationKey));
        for (String entry : string.split(";"))
        {
            String[] pair = entry.split(",");
            if (pair.length != 2) continue;
            int rawId = Integer.parseInt(pair[0]);
            int amplifier = Integer.parseInt(pair[1]);
            StatusEffect effect = StatusEffect.byRawId(rawId);
            if (effect == null) continue;
            map.put(effect, amplifier);
        }
        return map;
    }

    static String writeToString(Map<StatusEffect, StatusEffectInstance> map)
    {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<StatusEffect, StatusEffectInstance> entry : map.entrySet())
            if (entry.getKey() instanceof SyncEffect)
                builder.append(StatusEffect.getRawId(entry.getKey()))
                        .append(',')
                        .append(entry.getValue().getAmplifier())
                        .append(';');
        return builder.toString();
    }

    Map<StatusEffect, Integer> getSyncEffects();
}
