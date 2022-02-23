package me.spikey.specialarmour;

import com.google.common.collect.Lists;
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
import java.util.List;

public class EffectCommand implements CommandExecutor {
    private Main main;

    public EffectCommand(Main main) {
        this.main = main;
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

        if (PotionEffectType.getByName(args[1]) == null) {
            player.sendMessage("This is not a valid effect.");
            return true;
        }

        PotionEffectType potionEffectType = PotionEffectType.getByName(args[1]);

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(main.getKey(), PersistentDataType.BYTE_ARRAY)) {
            player.sendMessage("This item does not have any effects applied.");
            return true;
        }

        byte[] bytes = container.get(main.getKey(), PersistentDataType.BYTE_ARRAY);

        for (int x = 0; x < bytes.length; x++) {
            if (!PotionEffectType.values()[x].equals(potionEffectType)) continue;
            bytes[x] = 0;
        }

        container.set(main.getKey(), PersistentDataType.BYTE_ARRAY, bytes);

        itemStack.setItemMeta(itemMeta);

        player.sendMessage("Effect removed!");

        return true;
    }

    public boolean list(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(main.getKey(), PersistentDataType.BYTE_ARRAY)) {
            player.sendMessage("This item does not have any effects applied.");
            return true;
        }

        //List<String> effects = Lists.newArrayList();

        StringBuilder message = new StringBuilder("");

        byte[] bytes = container.get(main.getKey(), PersistentDataType.BYTE_ARRAY);

        for (int x = 0; x < bytes.length; x++) {
            if (bytes[x] == 0) continue;
            message.append(ChatColor.of(new Color(PotionEffectType.values()[x].getColor().asRGB()))).append(PotionEffectType.values()[x].getName()).append(" ").append(bytes[x]).append(ChatColor.GRAY).append(", ");
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

        PotionEffectType potionEffectType = PotionEffectType.getByName(args[1]);

        int level = 1;
        if (args.length != 2) {
            if (Integer.parseInt(args[2]) > 240) {
                player.sendMessage("You cannot apply an effect of that level.");
                return true;
            }
            level = Integer.parseInt(args[2]);
        }


        ItemStack itemStack = player.getInventory().getItemInMainHand();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (!container.has(main.getKey(), PersistentDataType.BYTE_ARRAY)) {

            List<Byte> bytes = Lists.newArrayList();

            for (int x = 0; x < PotionEffectType.values().length; x++) {
                bytes.add((byte) 0);
            }

            container.set(main.getKey(), PersistentDataType.BYTE_ARRAY, ArrayUtils.toPrimitive(bytes.toArray(new Byte[0])));
        }

        byte[] bytes = container.get(main.getKey(), PersistentDataType.BYTE_ARRAY);

        for (int x = 0; x < bytes.length; x++) {
            if (!PotionEffectType.values()[x].equals(potionEffectType)) continue;
            bytes[x] = (byte) level;
        }

        container.set(main.getKey(), PersistentDataType.BYTE_ARRAY, bytes);

        itemStack.setItemMeta(itemMeta);

        player.sendMessage("Effect applied!");

        return true;
    }

}
