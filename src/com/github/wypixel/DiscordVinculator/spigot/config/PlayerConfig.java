package com.github.wypixel.DiscordVinculator.spigot.config;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import com.github.wypixel.DiscordVinculator.spigot.config.database.MySQL;
import com.github.wypixel.DiscordVinculator.spigot.config.json.JSONConfig;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class PlayerConfig {

    public boolean sql = DiscordVinculator.getConnection() != null;
    public UUID uuid;
    public JSONConfig json;
    public File file;
    public String id;

    public PlayerConfig(String discord_id) {
        this.id = discord_id;
        load();
    }
    public PlayerConfig(UUID uuid) {
        this.uuid = uuid;
        load();
    }

    public String getID() {
        return id;
    }

    public boolean isSync() {
        return getID().length() > 5;
    }

    public void load() {
        if(sql) {
            MySQL mySQL = DiscordVinculator.getConnection();
            if(uuid != null && !DiscordVinculator.getAPI().exists(uuid)) {
                ZyleAPI.getAPI().getMySQL().executeAsyncUpdate("skywars", Arrays.asList("uuid", "statistics", "totalwins", "exp"),
                        Arrays.asList(player.getUniqueId().toString(), skywars.toString(), 0, 0));
                mySQL.executeAsyncUpdate(mySQL.table + "_players", "uuid", uuid.toString());
                return;
            }
            if(uuid != null) {
                if (mySQL.hasString(mySQL.table + "_players", "uuid", uuid.toString())) {
                    this.id = mySQL.getString(mySQL.table + "_players", "uuid", uuid.toString(), "discord_id");
                }
            } else if(this.id != null) {
                if (mySQL.hasString(mySQL.table + "_players", "discord_id", this.id)) {
                    this.uuid = UUID.fromString(mySQL.getString(mySQL.table + "_players", "discord_id", id, "uuid"));
                }
            }
            return;
        }
            file = new File(DiscordVinculator.getPlugin().getDataFolder() + "/" + "players/" + uuid.toString() + ".json");
            boolean exist = file.exists();
            if (!exist) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            json = new JSONConfig(file);
            if (!exist) {
                json.set("uuid", uuid.toString());
                json.set("discord_id", "");
                json.save();
                DiscordVinculator.getPlugin().getLogger().info("Created file: " + file.getName());
            }
            this.id = json.get("discord_id");
    }
}
