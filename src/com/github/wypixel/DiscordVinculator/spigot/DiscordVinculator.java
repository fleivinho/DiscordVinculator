package com.github.wypixel.DiscordVinculator.spigot;

import com.github.wypixel.DiscordVinculator.bot.DiscordBot;
import com.github.wypixel.DiscordVinculator.spigot.config.database.MySQL;
import com.github.wypixel.DiscordVinculator.spigot.plugin.WPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DiscordVinculator extends WPlugin {

    public static WPlugin plugin;
    public static VinculatorAPI api;
    public static MySQL mysql;

    @Override
    public void load() {
        plugin = this;
        try {
            loadConfig("messages");
            getLogger().info("Message configuration loaded!");
        } catch (Exception e) {
            getLogger().warning("Message configuration was not loaded! (" + e.getLocalizedMessage() + ")");
        }
    }

    @Override
    public void enable() {
        api = new VinculatorAPI();
        loadAll(this.getClass().getPackage().getName());;
        plugin.getLogger().info("Check: https://github.com/wypixel/DiscordVinculator");
        if(!plugin.getConfig().getBoolean("Use-MySQL")) {
            File file = new File(this.getDataFolder()+"/" + "players/");
            if(!file.exists()) {
                file.mkdirs();
            }
            plugin.getLogger().info("Creating/loading files...");
        } else {
            mysql = new MySQL(this,
                    plugin.getConfig().getString("MySQL.Host"),
                    plugin.getConfig().getString("MySQL.Usuario"),
                    plugin.getConfig().getString("MySQL.Senha"),
                    plugin.getConfig().getString("MySQL.Database"),
                    plugin.getConfig().getInt("MySQL.Port"));
            plugin.getLogger().info("Using MySQL...");
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void disable() {
        plugin = null;
    }

    public static FileConfiguration getMessages() {
        return plugin.getConfig("messages");
    }

    public static VinculatorAPI getAPI() {
        return api;
    }

    public static MySQL getConnection() {
        return mysql;
    }

    public static WPlugin getPlugin() {
        return plugin;
    }
}
