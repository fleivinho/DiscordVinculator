package com.github.wypixel.DiscordVinculator.spigot.commands;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import com.github.wypixel.DiscordVinculator.spigot.VinculatorAPI;
import com.github.wypixel.DiscordVinculator.spigot.config.PlayerConfig;
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
        VinculatorAPI api = DiscordVinculator.getAPI();
        if(api.getKeys().containsKey(((Player) sender).getUniqueId())) {
            sender.sendMessage("");
            sender.sendMessage("§7Já existe uma §aKey§7 para seu usuário!");
            sender.sendMessage("§7Key: §a" + api.getKeys().get(((Player) sender).getUniqueId()));
            sender.sendMessage("");
            return false;
        }
        if(args.length == 0) {
            sender.sendMessage("Utilize: §c/" + label + " key");
            return false;
        }
        if(args[0].equalsIgnoreCase("key")) {
            if(DiscordVinculator.getAPI().exists(((Player) sender).getUniqueId())) {
                sender.sendMessage("§cJá existe um usuário vinculado a sua conta!");
                return false;
            }
            String key = "123456";
            sender.sendMessage("§7Utilize a seguinte chave para vincular: §a'" + key + "'§7!");
            api.getKeys().put(((Player) sender).getUniqueId(), key);
            return false;
        }
        return false;
    }
}
