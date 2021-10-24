package com.github.wypixel.DiscordVinculator.spigot.config.database;

import java.sql.Connection;

public interface LoptySQLConnection {

	public Connection getConnection();

	public void openConnection();

	public void closeConnection();
	
}
