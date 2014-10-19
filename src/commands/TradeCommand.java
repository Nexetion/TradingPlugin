package commands;

import java.util.ArrayList;
import java.util.HashMap;

import listeners.GuiTrade;
import me.trading.TradingPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import util.Lang;

public class TradeCommand implements CommandExecutor{

	public HashMap<String, ArrayList<String>> requests = new HashMap();
	private TradingPlugin core;
	
	public TradeCommand(TradingPlugin core){
		this.core = core;
	}

	public boolean onCommand(CommandSender cs, Command cmd, String label, String args[]){

		Player p = (Player) cs;


		if(cmd.getName().equalsIgnoreCase("trade")){

			if(args.length == 0){
				
				/**
				 * Sends trading help message
				 */
				
				p.sendMessage(Lang.getMessage(Lang.TRADE_HELP));
				return true;
			
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("accept")){
					p.sendMessage(Lang.getMessage(Lang.COMMAND_UNKNOWN));
					return true;
				}

				/*
				 * Checking the player that the commandsender is requesting to trade with.
				 */
				
				Player target = Bukkit.getPlayer(args[0]);
				if(target == null){
					p.sendMessage(Lang.getMessage(Lang.PLAYER_NOTFOUND));
					return true;
				}

				if(target.getName().equals(p.getName())){
					p.sendMessage(Lang.getMessage(Lang.PLAYER_YOURSELF));
					return true;
				}

				if(this.requests.get(p.getName()) == null)
				{
					this.requests.put(p.getName(), new ArrayList());
				}
				ArrayList pRequest = (ArrayList)this.requests.get(p.getName());

				pRequest.add(target.getName());
				
				this.requests.put(p.getName(), pRequest);
				
				target.sendMessage(Lang.getMessage(Lang.TRADE_REQUEST_PLAYER).replaceAll("<requester>", p.getName()));
				
				p.sendMessage(Lang.getMessage(Lang.TRADE_REQUEST_REQUESTER).replaceAll("<player>", target.getName()));

			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("accept")){
					Player requester = Bukkit.getPlayer(args[1]);

					if(requester == null){
						p.sendMessage(Lang.getMessage(Lang.PLAYER_NOTFOUND));
						return true;
					}

					if(this.requests.get(requester.getName()) == null){
						p.sendMessage(Lang.getMessage(Lang.NO_TRADES));
						return true;
					}

					/*
					 * Requested Player Accepted now we are creating a new trade.
					 */
					
					GuiTrade trade = new GuiTrade(p, requester);
					
					/**
					 * Opens trading menu. 
					 */
					
					trade.openMenu(true);
					
					requester.sendMessage(Lang.getMessage(Lang.TRADE_ACCEPT_REQUESTER).replaceAll("<requested>", p.getName()));
					p.sendMessage(Lang.getMessage(Lang.TRADE_ACCEPT_PLAYER).replaceAll("<requested>", requester.getName()));
					
					this.requests.remove(p.getName());
					this.requests.remove(requester.getName());
					
				}
				
				return true;
				
			}else if(args[0].equalsIgnoreCase("accept")){
				p.sendMessage(Lang.getMessage(Lang.COMMAND_UNKNOWN));
				return true;
			}else{
				p.sendMessage(Lang.getMessage(Lang.COMMAND_UNKNOWN));
				return true;
			}

		}
		return true;
	}

}
