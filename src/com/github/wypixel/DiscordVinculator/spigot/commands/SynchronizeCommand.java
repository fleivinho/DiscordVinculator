package com.github.wypixel.DiscordVinculator.spigot.commands;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SynchronizeCommand extends Command {

    public String name = "";

    public SynchronizeCommand() {
        super(DiscordVinculator.getPlugin().getConfig().getString("Commands.synchronize.label"));
        if(!DiscordVinculator.getPlugin().getConfig().getString("Commands.synchronize.permission").isEmpty() && !DiscordVinculator.getPlugin().getConfig().getString("Commands.synchronize.permission").isBlank() ) {
            this.setPermission(DiscordVinculator.getPlugin().getConfig().getString("Commands.synchronize.permission"));
        }
        this.setAliases(DiscordVinculator.getPlugin().getConfig().getStringList("Commands.synchronize.aliases"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        if(args.length == 0) {
            sender.sendMessage("§c/" + label + " (nickname)");
            return false;
        }
        return false;
    }
}
