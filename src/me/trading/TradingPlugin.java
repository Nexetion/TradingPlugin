package me.trading;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import util.TextReader;

import commands.TradeCommand;


public class TradingPlugin extends JavaPlugin {
	
	Logger log = Logger.getLogger("Minecraft");
	
	public static TradingPlugin instance;
	
	public void onEnable(){
		log.info("Trade v1 Enabled!");
		
		instance = this;
		
		getCommand("trade").setExecutor(new TradeCommand(this));
		
	}
	
	public void onDisable(){
		log.info("Trade v1 disabled!");
	}
	
	public static TradingPlugin getInstance(){
		return instance;
	}
	
}
