package com.github.wypixel.DiscordVinculator.spigot;

import com.github.wypixel.DiscordVinculator.bot.DiscordBot;
import com.github.wypixel.DiscordVinculator.spigot.config.PlayerConfig;
import com.github.wypixel.DiscordVinculator.spigot.config.json.JSONConfig;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class VinculatorAPI {

    private DiscordBot bot;
    private HashMap<UUID, PlayerConfig> users = new HashMap<>();
    private HashMap<UUID, String> keys = new HashMap<>();

    public VinculatorAPI() {
        if(bot == null) {
            bot = new DiscordBot(DiscordVinculator.getPlugin().getConfig().getString("Bot-Prefix"),
                    DiscordVinculator.getPlugin().getConfig().getString("Bot-Token"),
                    DiscordVinculator.getPlugin().getConfig().getString("Guild-ID"));
        }
    }

    public boolean exists(UUID uuid) {
        if(DiscordVinculator.getConnection() == null) {
            return new File(DiscordVinculator.getPlugin().getDataFolder() + "/" + "users/" + uuid.toString() + ".json").exists();
        }
        ResultSet resultSet = DiscordVinculator.getConnection().executeQuery("SELECT * FROM " + DiscordVinculator.getConnection().getTable() + "_users" + " WHERE uuid= '" + uuid.toString() + "';");
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean exists(String discord_id) {
        if(DiscordVinculator.getConnection() == null) {
            for(PlayerConfig users : users.values()) {
                if(users.getID().equalsIgnoreCase(discord_id)) {
                    return true;
                }
            }
            return false;
        }
        ResultSet resultSet = DiscordVinculator.getConnection().executeQuery("SELECT * FROM " + DiscordVinculator.getConnection().getTable() + "_users" + " WHERE discord_id= '" + discord_id + "';");
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public HashMap<UUID, PlayerConfig> getUsers() {
        return users;
    }

    public HashMap<UUID, String> getKeys() {
        return keys;
    }

    public UUID getUUIDbyKey(String key) {
        for(UUID uuid : keys.keySet()) {
            if(keys.get(uuid).equalsIgnoreCase(key)) {
                return uuid;
            }
        }
        return null;
    }

    public boolean hasUser(String discord_id) {
        for(PlayerConfig users : users.values()) {
            if(users.getID().equalsIgnoreCase(discord_id)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUser(UUID uuid) {
        return users.containsKey(uuid);
    }

    public PlayerConfig getPlayerConfig(String discord_Id) {
        return new PlayerConfig(discord_Id);
    }

    public PlayerConfig getPlayerConfig(UUID uuid) {
        return new PlayerConfig(uuid);
    }

    public DiscordBot getBot() {
        return bot;
    }

    public boolean verify(UUID uuid) {
            if(DiscordVinculator.mysql == null) {
                File file = new File(DiscordVinculator.getPlugin().getDataFolder() + "/" + "users/" + uuid.toString() + ".json");
                if(file.exists()) {
                    JSONConfig config = new JSONConfig(file);
                    if (getBot().getGuild().getMemberById(config.get("discord_id")) == null) {
                        DiscordVinculator.getPlugin().getLogger().warning("Deleting file: " + file.getName() + "");
                        file.delete();
                        return false;
                    }
                }
            } else {
                if(DiscordVinculator.mysql.hasString(DiscordVinculator.mysql.getTable() + "_users", "uuid", uuid.toString())) {
                    if (getBot().getGuild().getMemberById(DiscordVinculator.mysql.get(DiscordVinculator.mysql.table + "_users", "uuid", uuid.toString(), "discord_id")) == null) {
                        DiscordVinculator.mysql.executeAsyncUpdate("DELETE FROM `" + DiscordVinculator.mysql.table + "_users` WHERE `uuid`='" + uuid.toString() + "'");
                        DiscordVinculator.getPlugin().getLogger().warning("Deleting user: " + uuid.toString() + "");
                        return false;
                    }
                }
            }
            return true;
        }
}
