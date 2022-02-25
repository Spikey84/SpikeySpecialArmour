package me.spikey.specialarmour.customEffects.effects;

import me.spikey.specialarmour.customEffects.Effect;
import org.bukkit.potion.PotionEffectType;


import java.awt.*;

public record PotionEffect(PotionEffectType potionEffectType, String name, byte id,
                           Color color) implements Effect {
}
