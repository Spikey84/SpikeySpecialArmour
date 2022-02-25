package me.spikey.specialarmour.customEffects.effects;

import me.spikey.specialarmour.customEffects.CustomEffect;
import org.bukkit.entity.Player;

import java.awt.*;

public class Magnetic implements CustomEffect {
    @Override
    public String name() {
        return "Magnetic";
    }

    @Override
    public byte id() {
        return 0;
    }

    @Override
    public Color color() {
        return new Color(166, 166, 166);
    }

    @Override
    public void apply(Player player, byte level) {
        player.sendMessage("MagnetWorks");
    }
}
