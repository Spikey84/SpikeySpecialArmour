package me.spikey.specialarmour;

import com.google.common.collect.Lists;
import me.spikey.specialarmour.customEffects.EffectManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffectTab implements TabCompleter {
    private EffectManager effectManager;

    public EffectTab(EffectManager effectManager) {
        this.effectManager = effectManager;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return Lists.newArrayList("add", "remove", "list");
        }

        if (args.length == 2 && (args[0].equals("add") || (args[0].equals("remove")))) {
            return effectManager.getEffectNames();
        }

        return Lists.newArrayList();
    }
}
