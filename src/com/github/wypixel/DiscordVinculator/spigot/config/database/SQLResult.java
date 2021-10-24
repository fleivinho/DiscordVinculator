package com.github.wypixel.DiscordVinculator.spigot.config.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SQLResult {

	public void process(ResultSet result) throws SQLException;

}
