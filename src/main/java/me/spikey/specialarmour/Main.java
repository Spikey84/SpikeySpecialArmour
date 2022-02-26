package me.spikey.specialarmour;

import com.google.common.collect.Maps;
import me.spikey.specialarmour.customEffects.Effect;
import me.spikey.specialarmour.customEffects.EffectManager;
import me.spikey.specialarmour.utils.ByteArrays;
import me.spikey.specialarmour.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Main extends JavaPlugin {
    public static NamespacedKey indexKey;
    public static NamespacedKey levelKey;
    private EffectManager effectManager;

    public static boolean lore;
    public static boolean stackPotionEffects;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        SchedulerUtils.setPlugin(this);
        indexKey = new NamespacedKey(this, "effectID");
        levelKey = new NamespacedKey(this, "effectLevel");

        FileConfiguration config = getConfig();

        lore = config.getBoolean("lore");
        stackPotionEffects = config.getBoolean("stackPotionEffects");

        this.effectManager = new EffectManager();


        Objects.requireNonNull(getCommand("itemEffect")).setExecutor(new EffectCommand(effectManager));
        Objects.requireNonNull(getCommand("itemEffect")).setTabCompleter(new EffectTab(effectManager));


        SchedulerUtils.runRepeating(() -> {
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();

            for (Player player : players) {
                HashMap<Byte, Byte> toEffect = Maps.newHashMap();

                if (player.getInventory().getArmorContents() == null) continue;

                for (ItemStack item : player.getInventory().getArmorContents()) {

                    if (item == null || item.getType().equals(Material.AIR)) continue;
                    ItemMeta itemMeta = item.getItemMeta();
                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                    if (!container.has(indexKey, PersistentDataType.BYTE_ARRAY)) continue;

                    ByteArrays byteArrays = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));


                    byte[] index = byteArrays.getIndex();

                    for (byte x = (byte) 0; x < index.length; x++) {
                        Effect effect = effectManager.getEffectFromID(index[x]);
                        byte originalLevel = toEffect.getOrDefault(index[x], (byte) 0);
                        if (effect.stack()) {

                            toEffect.put(index[x], (byte) (byteArrays.getLevels()[x] + originalLevel));
                        } else {
                            if (originalLevel < byteArrays.getLevels()[x]) toEffect.put(index[x], byteArrays.getLevels()[x]);
                        }

                    }
                }

                SchedulerUtils.runAsync(() -> {
                    for (Map.Entry<Byte, Byte> entry : toEffect.entrySet()) {
                        effectManager.runEffect(player, entry.getKey(), entry.getValue());
                    }
                });
            }

        }, 20);

    }
}
