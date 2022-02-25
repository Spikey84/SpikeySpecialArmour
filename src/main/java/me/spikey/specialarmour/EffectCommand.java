package me.spikey.specialarmour;

import com.google.common.collect.Lists;
import me.spikey.specialarmour.customEffects.Effect;
import me.spikey.specialarmour.customEffects.EffectManager;
import me.spikey.specialarmour.utils.ByteArrayUtils;
import me.spikey.specialarmour.utils.ByteArrays;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EffectCommand implements CommandExecutor {
    private EffectManager effectManager;

    public EffectCommand(EffectManager effectManager) {
        this.effectManager = effectManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be run by players.");
            return true;
        }

        //Objects.requireNonNull(player.getItemInUse()).get


        if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR) /* TODO:check this is an armour piece*/) {
            commandSender.sendMessage("The item in your hand must be a valid armour piece for this command to run.");
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage("Please enter a sub command.");
            return true;
        }

        if (args[0].equals("remove")) {
            return remove(player, args);
        } else if (args[0].equals("add")) {
            return add(player, args);
        } else {
            return list(player);
        }
    }

    public boolean remove(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("Please enter an effect to remove.");
            return true;
        }

        if (effectManager.getEffectFromName(args[1]) == null) {
            player.sendMessage("This is not a valid effect.");
            return true;
        }

        Effect effect = effectManager.getEffectFromName(args[1]);

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(Main.indexKey, PersistentDataType.BYTE_ARRAY)) {
            player.sendMessage("This item does not have any effects applied.");
            return true;
        }

        ByteArrays original = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));

        ByteArrays byteArrays = ByteArrayUtils.remove(original, effect.id());

        if (Arrays.equals(byteArrays.getIndex(), original.getIndex())) {
            player.sendMessage("This item does not have this effect.");
            return true;
        }

        container.set(Main.indexKey, PersistentDataType.BYTE_ARRAY, byteArrays.getIndex());
        container.set(Main.levelKey, PersistentDataType.BYTE_ARRAY, byteArrays.getLevels());

        itemStack.setItemMeta(itemMeta);

        player.sendMessage(ChatColor.GRAY + "Effect %s removed!".formatted(ChatColor.of(effect.color()) + effect.name() + ChatColor.GRAY));

        return true;
    }

    public boolean list(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(Main.indexKey, PersistentDataType.BYTE_ARRAY)) {
            player.sendMessage("This item does not have any effects applied.");
            return true;
        }

        StringBuilder message = new StringBuilder("");

        ByteArrays byteArrays = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));

        for (int x = 0; x < byteArrays.getIndex().length; x++) {
            Effect effect = effectManager.getEffectFromID(byteArrays.getIndex()[x]);
            message.append(ChatColor.of(effect.color())).append(effect.name()).append(" ").append(byteArrays.getLevels()[x]).append(ChatColor.GRAY).append(", ");
        }
        message.delete(message.length()-3, message.length());

        player.sendMessage(message.toString());

        return true;
    }

    public boolean add(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage("Please enter an effect to add.");
            return true;
        }

        if (PotionEffectType.getByName(args[1]) == null) {
            player.sendMessage("This is not a valid effect to apply to this item.");
            return true;
        }

        Effect effect = effectManager.getEffectFromName(args[1]);

        byte level = 1;
        if (args.length != 2) {
            if (Integer.parseInt(args[2]) > 240) {
                player.sendMessage("You cannot apply an effect of that level.");
                return true;
            }
            level = Byte.parseByte(args[2]);
        }


        ItemStack itemStack = player.getInventory().getItemInMainHand();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        ByteArrays byteArrays = null;
        if (!container.has(Main.indexKey, PersistentDataType.BYTE_ARRAY) || !container.has(Main.levelKey, PersistentDataType.BYTE_ARRAY)) {
            byteArrays = ByteArrayUtils.encode(effect.id(), level);
        } else {
            ByteArrays original = new ByteArrays(container.get(Main.indexKey, PersistentDataType.BYTE_ARRAY), container.get(Main.levelKey, PersistentDataType.BYTE_ARRAY));
            byteArrays = ByteArrayUtils.encode(original, effect.id(), level);
        }
        container.set(Main.indexKey, PersistentDataType.BYTE_ARRAY, byteArrays.getIndex());
        container.set(Main.levelKey, PersistentDataType.BYTE_ARRAY, byteArrays.getLevels());

        itemStack.setItemMeta(itemMeta);

        player.sendMessage("Effect applied!");

        return true;
    }

}
