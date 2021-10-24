package com.github.wypixel.DiscordVinculator.spigot.plugin;

import com.github.wypixel.DiscordVinculator.spigot.DiscordVinculator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public class BukkitLoader extends LoaderM {

	public BukkitLoader(Plugin plugin, String pack) {
		super(plugin, pack);
	}

	String pac = pack;
	Boolean scmd = true;
	JavaPlugin plugin = (JavaPlugin)instance;
	//public static List<Commands> commands = new ArrayList<Commands>();
    
	@Override
	public void load() {
		for (Class<?> classes : Getter.getClassesForPackage((JavaPlugin)plugin, pac)) {
			try {
				if (!classes.getName().contains("$") && Listener.class.isAssignableFrom(classes) && !classes.getName().equalsIgnoreCase(plugin.getName())) {
					Bukkit.getPluginManager().registerEvents((Listener) classes.newInstance(), plugin);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
		for (Class<?> classes : Getter.getClassesForPackage((JavaPlugin)plugin, pac)) {
			try {
				if (Command.class.isAssignableFrom(classes) && !classes.getSimpleName().equals("Command")) {
					Command command = (Command) classes.newInstance();
					((CraftServer) Bukkit.getServer()).getCommandMap().register(DiscordVinculator.getPlugin().getName(), command);
				}
			} catch (Exception exception) {
				plugin.getLogger().warning("Nao foi possivel carregar o comando " + classes.getSimpleName() + ". Causa: " + exception);
				exception.printStackTrace();
			}
		}
	}



}
