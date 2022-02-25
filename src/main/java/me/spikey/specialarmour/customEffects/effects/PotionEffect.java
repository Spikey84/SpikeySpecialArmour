package me.spikey.specialarmour.customEffects.effects;

import me.spikey.specialarmour.customEffects.Effect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class PotionEffect implements Effect {
    private PotionEffectType potionEffectType;
    private String name;
    private byte id;
    private Color color;

    public PotionEffect(PotionEffectType potionEffectType, String name, byte id,  Color color) {
        this.potionEffectType = potionEffectType;
        this.name = name;
        this.id = id;
        this.color = color;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public byte id() {
        return id;
    }

    @Override
    public Color color() {
        return null;
    }

    public PotionEffectType potionEffectType() {
        return potionEffectType;
    }
}
