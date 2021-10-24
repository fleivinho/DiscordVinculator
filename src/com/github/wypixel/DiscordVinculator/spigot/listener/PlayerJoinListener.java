package com.github.wypixel.DiscordVinculator.spigot.listener;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import com.github.wypixel.DiscordVinculator.spigot.config.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
            PlayerConfig playerConfig = new PlayerConfig(player.getUniqueId());
            if(!playerConfig.isSync()) {
                if(DiscordVinculator.getPlugin().getConfig().getBoolean("Join-Warn")) {
                    for(String line : DiscordVinculator.getMessages().getStringList("Join-Warn")) {
                        line = line.replace("&", "§");
                        line = line.replace("%nickname%", player.getName());
                        player.sendMessage(line);
                    }
                }
            }
        }
}
