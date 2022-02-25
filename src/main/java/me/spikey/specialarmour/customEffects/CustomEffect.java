package me.spikey.specialarmour.customEffects;

import org.bukkit.entity.Player;

import java.awt.*;


public interface CustomEffect extends Effect {


    void apply(Player player, byte level);
}
