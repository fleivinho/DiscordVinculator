package com.github.wypixel.DiscordVinculator.spigot.config.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoptySQLUpdateRunnable implements Runnable{

	private Connection connection;
	private LoptySQL sql;

	public LoptySQLUpdateRunnable(Connection connection, LoptySQL sql) {
		this.connection = connection;
		this.sql = sql;
	}

	public Connection getConnection() {
		return connection;
	}

	public void run() {
		try (PreparedStatement statement = connection.prepareStatement(sql.getSQL())) {
			sql.applyObjects(statement);
			
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new LoptySQLException("[SQL] Erro ao executar a query: "+sql.getSQL());
		}
	}

}
