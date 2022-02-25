package me.spikey.specialarmour.customEffects;

import org.bukkit.entity.Player;

public interface CustomEffect extends Effect {


    void apply(Player player, byte level);
}
