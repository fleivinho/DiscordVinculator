package com.github.wypixel.DiscordVinculator.spigot.config.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoptySQLRunnable implements Runnable{

	private Connection connection;
	private LoptySQL sql;
	private SQLResult action;

	public LoptySQLRunnable(Connection connection, LoptySQL sql, SQLResult action) {
		this.connection = connection;
		this.sql = sql;
		this.action = action;
	}

	public Connection getConnection() {
		return connection;
	}

	public LoptySQL getSQL() {
		return sql;
	}

	public void run() {
		try (PreparedStatement statement = connection.prepareStatement(sql.getSQL())) {
			sql.applyObjects(statement);

			try (ResultSet result = statement.executeQuery()) {
				action.process(result);
			}
		} catch (SQLException ex) {
			throw new LoptySQLException("Erro ao executar o SQL (" + sql.getSQL() + ")", ex);
		}
	}

}
