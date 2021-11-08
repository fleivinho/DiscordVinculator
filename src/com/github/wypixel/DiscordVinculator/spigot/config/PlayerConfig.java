package com.github.wypixel.DiscordVinculator.spigot.config;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import com.github.wypixel.DiscordVinculator.spigot.VinculatorAPI;
import com.github.wypixel.DiscordVinculator.spigot.config.database.MySQL;
import com.github.wypixel.DiscordVinculator.spigot.config.json.JSONConfig;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerConfig {

    public boolean sql = DiscordVinculator.getConnection() != null;
    public UUID uuid;
    public JSONConfig json;
    public File file;
    public String id;

    public PlayerConfig(String discord_id) {
        this.id = discord_id;
    }
    public PlayerConfig(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean exists() {
        if(uuid != null) {
            return DiscordVinculator.getAPI().exists(uuid);
        } else {
            return DiscordVinculator.getAPI().exists(id);
        }
    }

    public PlayerConfig setID(String id) {
        this.id = id;
        load();
        return this;
    }

    public String getID() {
        return id;
    }

    public void load() {
        DiscordVinculator.getAPI().verify(uuid);
        if(sql) {
            MySQL mySQL = DiscordVinculator.getConnection();
            if(uuid != null && !DiscordVinculator.getAPI().exists(uuid)) {
                mySQL.executeAsyncUpdate(mySQL.table + "_users", Arrays.asList("uuid", "discord_id"), Arrays.asList(uuid.toString(), this.id));
                DiscordVinculator.getPlugin().getLogger().info("Created user: " + uuid.toString());
                return;
            }
            if(uuid != null) {
                if (mySQL.hasString(mySQL.table + "_users", "uuid", uuid.toString())) {
                    this.id = mySQL.getString(mySQL.table + "_users", "uuid", uuid.toString(), "discord_id");
                }
            } else if(this.id != null) {
                if (mySQL.hasString(mySQL.table + "_users", "discord_id", this.id)) {
                    this.uuid = UUID.fromString(mySQL.getString(mySQL.table + "_users", "discord_id", id, "uuid"));
                }
            }
            return;
        }
        if(DiscordVinculator.getAPI().hasUser(uuid)) {
            return;
        }
            file = new File(DiscordVinculator.getPlugin().getDataFolder() + "/" + "users/" + uuid.toString() + ".json");
            boolean exist = file.exists();
           /* if (!exist) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
            json = new JSONConfig(file);
            if (!exist) {;
                json.set("uuid", uuid.toString());
                json.set("discord_id", this.id);
                json.save();
                DiscordVinculator.getPlugin().getLogger().info("Created file: " + file.getName());
            }
            this.id = json.get("discord_id");
            DiscordVinculator.getAPI().getUsers().put(uuid, this);
    }

    public Member getMember() {
        return DiscordVinculator.getAPI().getBot().getGuild().getMemberById(id);
    }
}
