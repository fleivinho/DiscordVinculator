package com.github.wypixel.DiscordVinculator.spigot;

import com.github.wypixel.DiscordVinculator.bot.DiscordBot;
import com.github.wypixel.DiscordVinculator.spigot.config.PlayerConfig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class VinculatorAPI {

    private DiscordBot bot;

    public VinculatorAPI() {

    }

    public boolean exists(UUID uuid) {
        ResultSet resultSet = DiscordVinculator.getConnection().executeQuery("SELECT * FROM " + DiscordVinculator.getConnection().getTable() + "_players" + " WHERE uuid= '" + uuid.toString() + "';");
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean exists(String discord_id) {
        ResultSet resultSet = DiscordVinculator.getConnection().executeQuery("SELECT * FROM " + DiscordVinculator.getConnection().getTable() + "_players" + " WHERE discord_id= '" + discord_id + "';");
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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

}
