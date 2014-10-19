package util;

import org.bukkit.ChatColor;

public enum Lang {
	
	TRADE_REQUEST_REQUESTER("&7You've requested to &6trade with <player>&7."),
	TRADE_REQUEST_PLAYER("&6<requester> &7has requested to trade with you."),
	TRADE_ACCEPT_PLAYER("&7You've accepted &6<requested> request to trade."),
	TRADE_ACCEPT_REQUESTER("&6<requested> &7has accepted your trade request."),
	COMMAND_UNKNOWN("&cThat is a unknown command."),
	PLAYER_NOTFOUND("&7That player was &6not found &7or is &6not online."),
	PLAYER_YOURSELF("&7You can't trade with your self silly."),
	NO_TRADES("&7I'm sorry but you don't have any trades."),
	TRADE_HELP("&6Improper usage. &7The correct usage is &6/trade <player> : accept <player>&7.");
	
	
	private String message;
	
	Lang(String message){
		this.message = message;;
	}
	
	public String getMessage() {
		return ChatColor.translateAlternateColorCodes('&', this.message);
	}
	
	
	public static String getMessage(Lang lang) {
		String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6M&8-&6Trade&8] ");
		return prefix + lang.getMessage();
	}
	
	
}
