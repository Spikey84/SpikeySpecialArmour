package me.spikey.specialarmour;

import com.google.common.collect.Lists;
import me.spikey.specialarmour.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Main extends JavaPlugin {
    private NamespacedKey key;
    private List<String> effectNames;


    @Override
    public void onEnable() {
        key = new NamespacedKey(this, "effects");
        SchedulerUtils.setPlugin(this);
        getCommand("armoureffect").setExecutor(new EffectCommand(this));
        getCommand("armoureffect").setTabCompleter(new EffectTab(this));

        effectNames = Lists.newArrayList();

        for (PotionEffectType potionEffectType : PotionEffectType.values()) {
            effectNames.add(potionEffectType.getName());
        }



        SchedulerUtils.runRepeating(() -> {

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getInventory().getArmorContents() == null) continue;
                for (ItemStack item : player.getInventory().getArmorContents()) {
                    if (item == null) continue;
                    ItemMeta itemMeta = item.getItemMeta();
                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                    if (!container.has(key, PersistentDataType.BYTE_ARRAY)) continue;

                    byte[] effects = container.get(key, PersistentDataType.BYTE_ARRAY);

                    for (byte x = (byte) 0; x < effects.length; x++) {
                        if (effects[x] == (byte) 0) continue;
                        player.addPotionEffect(new PotionEffect(PotionEffectType.values()[x], 40, effects[x] - 1));
                    }
                }
            }

        }, 20);
    }

    public NamespacedKey getKey() {
        return key;
    }

    public List<String> getEffectNames() {
        return effectNames;
    }
}
