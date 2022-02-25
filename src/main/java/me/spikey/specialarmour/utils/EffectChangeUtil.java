package me.spikey.specialarmour.utils;

import me.spikey.specialarmour.Main;
import me.spikey.specialarmour.customEffects.Effect;
import me.spikey.specialarmour.customEffects.EffectManager;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EffectChangeUtil {

    public static response AddEffect(ItemStack itemStack, Effect effect, byte level) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        ByteArrays byteArrays;
        if (!container.has(Main.indexKey, PersistentDataType.BYTE_ARRAY) || !container.has(Main.levelKey, PersistentDataType.BYTE_ARRAY)) {
            byteArrays = ByteArrayUtils.encode(effect.id(), level);
        } else {
            ByteArrays original = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));

            if (ArrayUtils.contains(original.getIndex(), effect.id())) {
                byteArrays = ByteArrayUtils.remove(original, effect.id());
                byteArrays = ByteArrayUtils.encode(byteArrays, effect.id(), level);

                container.set(Main.indexKey, PersistentDataType.BYTE_ARRAY, byteArrays.getIndex());
                container.set(Main.levelKey, PersistentDataType.BYTE_ARRAY, byteArrays.getLevels());

                itemStack.setItemMeta(itemMeta);

                return response.LEVEL_CHANGED;
            }

            byteArrays = ByteArrayUtils.encode(original, effect.id(), level);
        }
        container.set(Main.indexKey, PersistentDataType.BYTE_ARRAY, byteArrays.getIndex());
        container.set(Main.levelKey, PersistentDataType.BYTE_ARRAY, byteArrays.getLevels());

        itemStack.setItemMeta(itemMeta);

        return response.EFFECT_ADDED;
    }

    public static response effectRemove(ItemStack itemStack, Effect effect) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(Main.indexKey, PersistentDataType.BYTE_ARRAY)) {
            return response.NO_EFFECTS;
        }

        ByteArrays original = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));

        if (!ArrayUtils.contains(original.getIndex(), effect.id())) {
            return response.NO_EFFECT;
        }

        ByteArrays byteArrays = ByteArrayUtils.remove(original, effect.id());


        container.set(Main.indexKey, PersistentDataType.BYTE_ARRAY, byteArrays.getIndex());
        container.set(Main.levelKey, PersistentDataType.BYTE_ARRAY, byteArrays.getLevels());

        itemStack.setItemMeta(itemMeta);

        return response.EFFECT_REMOVED;
    }

    public static EffectListResponse listEffects(ItemStack itemStack, EffectManager effectManager) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(Main.indexKey, PersistentDataType.BYTE_ARRAY)) {
            return new EffectListResponse(response.NO_EFFECTS, null);
        }

        ByteArrays byteArrays = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));

        return new EffectListResponse(response.SUCCESS, ByteArrayUtils.decode(byteArrays, effectManager));
    }

    public enum response {
        LEVEL_CHANGED("Level has been changed."),
        EFFECT_ADDED("Effect has been added."),
        NO_EFFECTS("There are no effects on this item."),
        NO_EFFECT("This effect is not on this item."),
        EFFECT_REMOVED("This effect has been removed."),
        SUCCESS("Success!");
        private final String string;

        response(String string) {
            this.string = string;
        }

        public void sendResponse(Player player) {
            player.sendMessage(ChatColor.GRAY + string);
        }
    }
}
