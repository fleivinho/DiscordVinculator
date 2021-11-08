package com.github.wypixel.DiscordVinculator.spigot.config.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MySQL implements LoptySQLConnection{

	private static final long KEEP_ALIVE_DELAY = 1000L * 60L * 4L;
	public static final ConsoleCommandSender console = Bukkit.getConsoleSender();
	private String host;
	private String user;
	private String password;
	private String database;
	public ArrayList<String> tablesNames = new ArrayList<String>();
	public ArrayList<String> tableStatements = new ArrayList<String>();
	private int port;
	private Connection slave;
	private Connection connection;
	private JavaPlugin plugin;
	public String table = "vinculator";
	
	public MySQL(JavaPlugin plugin, String host, String user, String password, String database, int port) {
		super();
		this.plugin = plugin;
		this.host = host;
		this.user = user;
		this.password = password;
		this.database = database;
		this.port = port;
		try {
			slave = DriverManager.getConnection("jdbc:mysql://" + host +":" + port + "/" + database +"?autoReconnect=true&failOverReadOnly=false&maxReconnects=2147483645", user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection() {
		return connection;
	}

	public String getTable() {
		return table;
	}

	public synchronized void executeAsyncUpdate(String table, List<String> into, List<Object> changes) {
		String t = "";
		List<String> into2 = new ArrayList<String>();
		for(String m : into) {
			into2.add("`" + m + "`");
		}
		List<Object> changes2 = new ArrayList<Object>();
		for(Object m : changes) {
			changes2.add("'" + m.toString() + "'");
		}

		String intoresult = into2.toString().substring(1, into2.toString().length()-1);
		String changesresult = changes2.toString().substring(1, changes2.toString().length()-1);
		executeAsyncUpdate("INSERT INTO `"+t + table +"`"+""
				+ " (" + intoresult + ") VALUES (" + changesresult +");");
	}

	public void updateInt(String identifier, String name, String table, String args, int args1) {
		PreparedStatement stm = null;
		try {
			stm = getConnection().prepareStatement("UPDATE `" + table + "` SET `" + args + "` = ? WHERE `" + identifier +"` = ?");
			stm.setInt(1, args1);
			stm.setString(2, name);
			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateString(String identifier, String name, String table, String args, String args1) {
		PreparedStatement stm = null;
		try {
			stm = connection.prepareStatement("UPDATE `" + table + "` SET `" + args + "` = ? WHERE `"+ identifier+"` = ?");
			stm.setString(1, args1);
			stm.setString(2, name);
			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateString(String nick, String table, String args, String args1) {
		PreparedStatement stm = null;
		nick = nick.toLowerCase();
		try {
			stm = connection.prepareStatement("UPDATE `" + table + "` SET `" + args + "` = ? WHERE `uuid` = ?");
			stm.setString(1, args1);
			stm.setString(2, nick);
			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void updateInt(String nick, String table, String args, int args1) {
		PreparedStatement stm = null;
		nick = nick.toLowerCase();
		try {
			stm = getConnection().prepareStatement("UPDATE `" + table + "` SET `" + args + "` = ? WHERE `uuid` = ?");
			stm.setInt(1, args1);
			stm.setString(2, nick);
			stm.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getIntegerLoptyFind(String tabela, String find, String find2, String procurando) {
		return Integer.parseInt((String) get(tabela, find, find2, procurando));
	}
	public int getInteger(String table, String find, String find2, String procurando) {
		return Integer.parseInt((String) get(table, find, find2, procurando));
	}
	public String getString(String table, String where, String whereTo, String line) {
		return get(table, where, whereTo, line);
	}
	  
	public ResultSet executeQuery(String query) {
		try {
			if (getConnection().isClosed()) {
				DiscordVinculator.getPlugin().getLogger().warning("Impossivel de realizar a query ('" + query + "') pois a conexao esta encerrada.");
			}
			return getConnection().createStatement().executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public boolean contains(String table, String where, String whereTo) {
		try {
			ResultSet set = executeQuery("SELECT * FROM `" + table + "` WHERE `" + where + "`='" + whereTo + "';");
			if (!set.next())
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public String get(String table, String where, String whereTo, String line) {
		try {
			if (contains(table, where, whereTo)) {
				ResultSet set = executeQuery("SELECT * FROM `" + table + "` WHERE `" + where + "`='" + whereTo + "';");
				if (!set.next())
					return new String();
				return set.getString(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String();
	}
	
	
	public Connection getSlaveConnection() {
		return slave;
	}
	
	public synchronized void executeAsyncUpdate(String update) {
		try {
			Statement s = getSlaveConnection().createStatement();
			s.executeUpdate(update);
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void runUpdate(LoptySQLUpdateRunnable sql, boolean asynchronously) {
		if (asynchronously) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					sql.run();
				}
			}.runTaskAsynchronously(JavaPlugin.getPlugin(plugin.getClass()));
		}else {
			sql.run();
		}
	}
	
	public void runSQL(LoptySQLRunnable sql, boolean asynchronously) {
		if (asynchronously) {
            new BukkitRunnable() {
				
				@Override
				public void run() {
					sql.run();
				}
			}.runTaskAsynchronously(JavaPlugin.getPlugin(plugin.getClass()));
		} else {
			sql.run();
		}
	}

	public void addTableStatement(String tableQuery) {
		this.tablesNames.add(tableQuery);
	}
	
	@Override
	public void openConnection() {
		if (connection != null) {
			DiscordVinculator.getPlugin().getLogger().warning("Erro ao iniciar MySQL!");
			return;
		}
		try {
			String link = "jdbc:mysql://" + host +":" + port + "/" + database +"?autoReconnect=true&failOverReadOnly=false&maxReconnects=2147483645";
			this.connection = DriverManager.getConnection(link, user, password);
			createTable();
			keepAlive();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void prepare() {
		this.tablesNames.add(table);
		this.tableStatements.add("CREATE TABLE IF NOT EXISTS `"+table+"_users`" + " ( `uuid` TEXT NOT NULL, `discord_id` TEXT NOT NULL);");
	}

	private void keepAlive() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(String table : MySQL.this.tablesNames) {
				 try (PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM "+table+";")) {
					 statement.executeQuery();
					 
				 } catch (SQLException e) {
					e.printStackTrace();
				}
				}
			}
		}.runTaskTimerAsynchronously(JavaPlugin.getPlugin(plugin.getClass()), KEEP_ALIVE_DELAY, KEEP_ALIVE_DELAY);
	}

	@Override
	public void closeConnection() {
		if(connection == null) {
			DiscordVinculator.getPlugin().getLogger().warning("A conexao MySQL ja foi encerrada!");
			return;
		}
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createTable() {
		if(connection!=null) {
			if(this.tablesNames.size() > 0) {
				for(String tableStatement : this.tableStatements) {
					try(Statement stmt = connection.createStatement()){
						stmt.executeUpdate(tableStatement);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}else {
				DiscordVinculator.getPlugin().getLogger().info("Nenhuma tabela MySQL foi efetuada!");
			}
		}else {
			DiscordVinculator.getPlugin().getLogger().info("Nao foi possivel criar uma tabela, a conexao MySQL nao foi aberta!");
		}
	}

	public boolean hasString(String table, String ofInfo, String target) {
		PreparedStatement stm = null;
		try {
			stm = getConnection().prepareStatement("SELECT * FROM `" + table + "` WHERE `" + ofInfo +"` = ?");
			stm.setString(1, target);
			ResultSet rs = stm.executeQuery();
			if (rs.next())
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


	public synchronized void createTables(String table) {
		try {
			executeUpdate(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void executeUpdate(String update) {
		try {
			Statement s = getSlaveConnection().createStatement();
			s.executeUpdate(update);
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
