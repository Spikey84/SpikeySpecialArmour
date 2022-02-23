package me.spikey.specialarmour;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffectTab implements TabCompleter {
    private Main main;

    public EffectTab(Main main) {
        this.main = main;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return Lists.newArrayList("add", "remove", "list");
        }

        if (args.length == 2 && (args[0].equals("add") || (args[0].equals("remove")))) {
            return main.getEffectNames();
        }

        return Lists.newArrayList();
    }
}
