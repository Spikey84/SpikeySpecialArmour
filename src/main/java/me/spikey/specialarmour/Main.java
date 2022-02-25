package me.spikey.specialarmour;

import me.spikey.specialarmour.customEffects.EffectManager;
import me.spikey.specialarmour.utils.ByteArrays;
import me.spikey.specialarmour.utils.SchedulerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Main extends JavaPlugin {
    public static NamespacedKey indexKey;
    public static NamespacedKey levelKey;
    private EffectManager effectManager;


    @Override
    public void onEnable() {
        SchedulerUtils.setPlugin(this);
        indexKey = new NamespacedKey(this, "effectID");
        levelKey = new NamespacedKey(this, "effectLevel");

        this.effectManager = new EffectManager();


        Objects.requireNonNull(getCommand("armoureffect")).setExecutor(new EffectCommand(effectManager));
        Objects.requireNonNull(getCommand("armoureffect")).setTabCompleter(new EffectTab(effectManager));


        SchedulerUtils.runRepeating(() -> {

            for (Player player : Bukkit.getOnlinePlayers()) {

                if (player.getInventory().getArmorContents() == null) continue;

                for (ItemStack item : player.getInventory().getArmorContents()) {

                    if (!item.getType().equals(Material.AIR)) continue;
                    ItemMeta itemMeta = item.getItemMeta();
                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                    if (!container.has(indexKey, PersistentDataType.BYTE_ARRAY)) continue;

                    ByteArrays byteArrays = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));


                    byte[] index = byteArrays.getIndex();

                    for (byte x = (byte) 0; x < index.length; x++) {
                        effectManager.runEffect(player, index[x], byteArrays.getLevels()[x]);
                    }
                }
            }

        }, 20);

    }
}
