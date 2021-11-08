package com.github.wypixel.DiscordVinculator.spigot;

import com.github.wypixel.DiscordVinculator.bot.DiscordBot;
import com.github.wypixel.DiscordVinculator.spigot.config.database.MySQL;
import com.github.wypixel.DiscordVinculator.spigot.config.json.JSONConfig;
import com.github.wypixel.DiscordVinculator.spigot.plugin.WPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.font.FontRenderContext;
import java.io.File;
import java.io.IOException;

public class DiscordVinculator extends WPlugin {

    public static WPlugin plugin;
    public static VinculatorAPI api;
    public static MySQL mysql;

    @Override
    public void load() {
        plugin = this;
        try {
            loadConfig("messages");
            getLogger().info("Arquivo de mensagens carregada!");
        } catch (Exception e) {
            getLogger().warning("Arquivo de mensagens nao carregada! (" + e.getLocalizedMessage() + ")");
        }
    }

    @Override
    public void enable() {
        api = new VinculatorAPI();
        loadAll(this.getClass().getPackage().getName());;
        plugin.getLogger().info("Check: https://github.com/wypixel/DiscordVinculator");
        if(!plugin.getConfig().getBoolean("Use-MySQL")) {
            File file = new File(this.getDataFolder()+"/" + "users/");
            File format = new File(DiscordVinculator.getPlugin().getDataFolder() + "/" + "default-format.json");
            if(!file.exists()) {
                file.mkdirs();
            }
            JSONConfig config = new JSONConfig(format);
            plugin.getLogger().info("Criando/carregando arquivos...");
        } else {
            mysql = new MySQL(this,
                    plugin.getConfig().getString("MySQL.Host"),
                    plugin.getConfig().getString("MySQL.User"),
                    plugin.getConfig().getString("MySQL.Password"),
                    plugin.getConfig().getString("MySQL.Database"),
                    plugin.getConfig().getInt("MySQL.Port"));
            mysql.prepare();
            mysql.openConnection();
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
