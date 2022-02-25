package me.spikey.specialarmour;

import me.spikey.specialarmour.customEffects.Effect;
import me.spikey.specialarmour.customEffects.EffectManager;
import me.spikey.specialarmour.utils.EffectChangeUtil;
import me.spikey.specialarmour.utils.EffectListResponse;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EffectCommand implements CommandExecutor {
    private final EffectManager effectManager;

    public EffectCommand(EffectManager effectManager) {
        this.effectManager = effectManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.GRAY + "This command can only be run by players.");
            return true;
        }

        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR) /* TODO:check this is an armour piece*/) {
            commandSender.sendMessage(ChatColor.GRAY + "The item in your hand must be a valid armour piece for this command to run.");
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.GRAY + "Please enter a sub command.");
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
            player.sendMessage(ChatColor.GRAY + "Please enter an effect to remove.");
            return true;
        }

        if (effectManager.getEffectFromName(args[1]) == null) {
            player.sendMessage(ChatColor.GRAY + "This is not a valid effect.");
            return true;
        }

        Effect effect = effectManager.getEffectFromName(args[1]);

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        EffectChangeUtil.effectRemove(itemStack, effect).sendResponse(player);
        return true;
    }

    public boolean list(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        EffectListResponse responsePacket = EffectChangeUtil.listEffects(itemStack, effectManager);

        if (responsePacket.response().equals(EffectChangeUtil.response.NO_EFFECTS)) {
            player.sendMessage(ChatColor.GRAY + "There are no effects on this item.");
            return true;
        }

        HashMap<Effect, Byte> effects = responsePacket.effects();

        StringBuilder message = new StringBuilder();


        for (Map.Entry<Effect, Byte> entry : effects.entrySet()) {
            Effect effect = entry.getKey();
            byte level = entry.getValue();
            message.append(ChatColor.of(effect.color())).append(effect.name()).append(" ").append(level).append(ChatColor.GRAY).append(", ");
        }
        message.delete(message.length()-3, message.length());

        player.sendMessage(message.toString());
        return true;
    }

    public boolean add(Player player, String[] args) {

        if (args.length < 2) {
            player.sendMessage(ChatColor.GRAY + "Please enter an effect to add.");
            return true;
        }

        if (effectManager.getEffectFromName(args[1]) == null) {
            player.sendMessage(ChatColor.GRAY + "This is not a valid effect to apply to this item.");
            return true;
        }

        Effect effect = effectManager.getEffectFromName(args[1]);

        byte level = 1;
        if (args.length != 2) {
            if (Integer.parseInt(args[2]) > 240) {
                player.sendMessage(ChatColor.GRAY + "You cannot apply an effect of that level.");
                return true;
            }
            level = Byte.parseByte(args[2]);
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        EffectChangeUtil.AddEffect(itemStack, effect, level).sendResponse(player);
        return true;
    }

}
