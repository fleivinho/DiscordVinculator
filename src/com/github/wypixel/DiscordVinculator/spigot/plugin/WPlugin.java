package com.github.wypixel.DiscordVinculator.spigot.plugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Level;

public abstract class WPlugin extends JavaPlugin {

    private boolean loading;

    public WPlugin() {
        this.start();
    }

    public void loadAll(String pack) {
        new BukkitLoader(this, pack).load();
    }

    public abstract void start();

    public abstract void load();

    public abstract void enable();

    public abstract void disable();

    public void setEnable(boolean enable) {
        if(enable) {
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            Bukkit.getPluginManager().enablePlugin(this);
        }
    }

    @Override
    public void onLoad() {
        this.setLoading(true);
        this.load();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.enable();
        //  this.getLogger().info("Plugin inicializado!");
    }

    @Override
    public void onDisable() {
        this.disable();
    }

    public void loadConfig(String... files) {
        for(String m : files) {
            loadConfig(m);
        }
    }
    public FileConfiguration getConfig(String name) {
        File customYml = new File(this.getDataFolder()+"/" + name+ ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        return customConfig;
    }

    public FileConfiguration getConfig(String pac, String name) {
        File customYml = new File(this.getDataFolder()+"/" + pac + "/" + name+ ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        return customConfig;
    }

    public void loadConfig(String nome) {
        File customYml = new File(this.getDataFolder()+"/" + nome+ ".yml");
        FileConfiguration customConfig = YamlConfiguration.loadConfiguration(customYml);
        saveCustomYml(customConfig, customYml);
    }

    public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();
        File file = ymlFile;

        if (!file.exists()) {
            try (InputStream in = this.getResource(ymlFile.getName())) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            try {
                ymlConfig.load(new File(this.getDataFolder(), ymlFile.getName()));
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

}
