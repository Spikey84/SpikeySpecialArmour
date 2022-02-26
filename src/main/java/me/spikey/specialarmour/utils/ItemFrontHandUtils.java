package me.spikey.specialarmour.utils;

import com.google.common.collect.Lists;
import me.spikey.specialarmour.Main;
import me.spikey.specialarmour.customEffects.Effect;
import me.spikey.specialarmour.customEffects.EffectManager;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFrontHandUtils {
    private static final String[] romanNumerals = new String[]{"0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"};

    public static void setEffectLore(ItemStack itemStack, EffectManager effectManager) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        ByteArrays byteArrays = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));

        SchedulerUtils.runAsync(() -> {
            HashMap<Effect, Byte> effects = ByteArrayUtils.decode(byteArrays, effectManager);

            List<String> lines = Lists.newArrayList();

            for (Map.Entry<Effect, Byte> entry : effects.entrySet()) {
                Effect effect = entry.getKey();
                String name = effect.name().replace("_", " ");

                name = StringUtils.capitalize(name);

                String level;
                if(entry.getValue() > 20) level = String.valueOf(entry.getValue());
                else level = romanNumerals[entry.getValue()];


                String s =  ChatColor.of(effect.color()) + name + " " + level;
                lines.add(s);
            }
            SchedulerUtils.runSync(() -> {
                itemMeta.setLore(lines);
                itemStack.setItemMeta(itemMeta);
            });
        });
    }

}
