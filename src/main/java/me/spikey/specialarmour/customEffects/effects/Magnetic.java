package me.spikey.specialarmour.customEffects.effects;

import me.spikey.specialarmour.customEffects.CustomEffect;
import me.spikey.specialarmour.utils.SchedulerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.Collection;

public class Magnetic implements CustomEffect {
    @Override
    public String name() {
        return "Magnetic";
    }

    @Override
    public byte id() {
        return 100;
    }

    @Override
    public Color color() {
        return new Color(166, 166, 166);
    }

    @Override
    public boolean stack() {
        return true;
    }

    @Override
    public void apply(Player player, byte level) {

        Collection<Entity> entities = player.getNearbyEntities(level * 0.5, level * 0.5, level * 0.5);
        SchedulerUtils.runAsync(() -> {
            for (Entity entity : entities) {
                if (!(entity instanceof Item item)) continue;
                Vector to = player.getLocation().toVector();
                Location from = item.getLocation();
                Vector direction = to.subtract(from.toVector()).normalize();
                SchedulerUtils.runSync(() -> item.teleport(from.add(direction)));

                SchedulerUtils.runLater(() -> {
                    Vector toNew = player.getLocation().toVector();
                    Location fromNew = item.getLocation();
                    Vector newDirection = toNew.subtract(fromNew.toVector()).normalize();
                    item.teleport(fromNew.add(newDirection));
                    }, 20);
            }
        });

    }
}
