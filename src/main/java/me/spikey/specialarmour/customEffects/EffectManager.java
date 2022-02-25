package me.spikey.specialarmour.customEffects;

import com.google.common.collect.Lists;
import me.spikey.specialarmour.customEffects.effects.Magnetic;
import me.spikey.specialarmour.customEffects.effects.PotionEffect;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.util.List;
import java.util.Locale;

public class EffectManager {
    private final List<Effect> effects;
    private List<String> names;

    public EffectManager() {
        effects = Lists.newArrayList();
        names = Lists.newArrayList();

        int x = 0;
        for (PotionEffectType potionEffectType : PotionEffectType.values()) {
            effects.add(new PotionEffect(potionEffectType, potionEffectType.getName(), (byte) x, new Color(potionEffectType.getColor().asRGB())));
            x++;
        }

        registerEffect(new Magnetic());
    }

    public void registerEffect(CustomEffect effect) {
        effects.add(effect);
    }

    public List<String> getEffectNames() {
        if (names.size() != effects.size()) {
            names = Lists.newArrayList();
            effects.forEach((effect -> names.add(effect.name())));
        }
        return names;
    }

    public Effect getEffectFromName(String name) {
        for (Effect effect : effects) {
            if (!effect.name().toLowerCase(Locale.ROOT).equals(name.toLowerCase(Locale.ROOT))) continue;
            return effect;
        }
        return null;
    }

    public Effect getEffectFromID(byte id) {
        for (Effect effect : effects) {
            if (effect.id() != id) continue;
            return effect;
        }
        return null;
    }

    public void runEffect(Player player, byte id, byte level) {
        Effect effect = getEffectFromID(id);

        if (effect == null) {
            Bukkit.getLogger().info("Unknown Effect: %s".formatted(String.valueOf(id)));
            return;
        }

        if (effect instanceof PotionEffect) {
            PotionEffectType potionEffectType = ((PotionEffect) effect).potionEffectType();

            player.addPotionEffect(new org.bukkit.potion.PotionEffect(potionEffectType, 40, level-1));
        } else if (effect instanceof CustomEffect) {
            ((CustomEffect) effect).apply(player, level);
        } else {
            Bukkit.getLogger().info("Unknown effect type!");
        }
    }
}
