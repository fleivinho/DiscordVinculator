package com.github.wypixel.DiscordVinculator.spigot.plugin;

public abstract class LoaderM {

	Object instance;
	String pack;
	
	public LoaderM(Object instance, String pack) {
		this.instance = instance;
		this.pack = pack;
	}
	
	public abstract void load();
	
	public String getPackage() {
		return pack;
	}
	
	public Object getPlugin() {
		return instance;
	}
	
}
