package listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.trading.TradingPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import util.BiMap;
import util.ItemBuilder;
import util.Util;


public class GuiTrade implements Listener {

	private TradingPlugin core = TradingPlugin.getInstance();

	public static BiMap<UUID, GuiTrade> playerTraders = new BiMap<UUID, GuiTrade>();

	private Inventory tradeInventory = null;
	private UUID player1 = null, player2 = null;
	private boolean confirmed1 = false, confirmed2 = false;

	private List<Integer> player1Slots = null, player2Slots = null;

	/**
	 * GUITrade Constructor, creates a new GuiTrade.
	 */
	
	public GuiTrade(Player player1, Player player2) {
		if (player1 != null) this.player1 = player1.getUniqueId();
		if (player2 != null) this.player2 = player2.getUniqueId();
	}

	/**
	 * 
	 * AddItems to the approriate Inventory.
	 * 
	 * @param player Player ID
	 * @param itemStack ItemStack being added.
	 * @return true/false
	 */
	
	public boolean addItem(int player, ItemStack itemStack) {
		if (this.tradeInventory != null) {
			List<Integer> slotsList = player == 1 ? this.player1Slots : (player == 2 ? this.player2Slots : new ArrayList<Integer>());
			for (Integer slot : slotsList) {
				try {
					ItemStack slotItem = this.tradeInventory.getItem(slot);
					if (slotItem == null || slotItem.getType() == Material.AIR) {
						this.tradeInventory.setItem(slot, itemStack);
						return true;
					}
				} catch (Exception ex) {
					continue;
				}
			}
		}
		return false;
	}

	/*
	 * Checks if a player canClick the itemSlot.
	 */
	public boolean canClick(int player, int itemSlot) {
		return itemSlot < (this.tradeInventory != null ? this.tradeInventory.getSize() : 54) && itemSlot > 8 && (player == 1 ? itemSlot % 9 < 4 : (player == 2 && itemSlot % 9 > 4));
	}

	public boolean canClickItem(int player, int itemSlot) {
		return (player == 1 && itemSlot == 3) || (player == 2 && itemSlot == 5);
	}

	/*
	 * Closes the GUITrade Menu and unregisters the events.
	 */
	
	public void closeMenu(boolean unregisterEvents, boolean closeInventory) {
		this.closeMenu(unregisterEvents, closeInventory, closeInventory);
	}
	
	/*
	 * Acutal method that closes the GUI Trading Menu.
	 */

	public void closeMenu(boolean unregisterEvents, boolean closeInventory1, boolean closeInventory2) {
		if (this.tradeInventory != null){
			
			this.tradeInventory.clear();
		}
		
		
		if (unregisterEvents){
			HandlerList.unregisterAll(this);
		}
		
		
		if (closeInventory1) {
			Player player1 = this.getPlayer1();
			if (player1 != null) player1.closeInventory();
		}
		
		if (closeInventory2) {
			Player player2 = this.getPlayer2();
			if (player2 != null) player2.closeInventory();
		}
		
		
		playerTraders.remove(this.player1);
		playerTraders.remove(this.player2);
	}

	/**
	 * Gets all items on the the players side 
	 * @param player Player ID of the player trading.
	 * @return ItemStack Array.
	 */
	public ItemStack[] getItems(int player) {
		List<ItemStack> items = new ArrayList<ItemStack>();
		if (this.tradeInventory != null) {
			ItemStack[] itemContents = this.tradeInventory.getContents();
			for (int i = 0; i < this.tradeInventory.getSize(); i++) {
				if (this.canClick(player, i)) {
					if (itemContents[i] != null && itemContents[i].getType() != Material.AIR)
						items.add(itemContents[i]);
				}
			}
		}
		return items.toArray(new ItemStack[items.size()]);
	}

	/*
	 * @return Player Player1
	 */
	
	public Player getPlayer1() {
		return this.player1 != null ? Bukkit.getServer().getPlayer(this.player1) : null;
	}
	
	/**
	 * 
	 * @return Player Player2
	 */

	public Player getPlayer2() {
		return this.player2 != null ? Bukkit.getServer().getPlayer(this.player2) : null;
	}

	public void openMenu() {
		this.openMenu(true);
	}
	
	/**
	 * Opens the menu for each player.
	 * @param registerEvents
	 */

	public void openMenu(boolean registerEvents) {
		
		Player player1 = this.getPlayer1();
		
		Player player2 = this.getPlayer2();
		
		if (player1 != null && player2 != null && !playerTraders.containsKey(this.player1) && !playerTraders.containsKey(this.player2)) {
			this.closeMenu(true, true);

			this.setupInventory(player1, player2);

			if (registerEvents)
				Bukkit.getServer().getPluginManager().registerEvents(this, core);

			playerTraders.put(this.player1, this);
			playerTraders.put(this.player2, this);

			player1.openInventory(this.tradeInventory);
			player2.openInventory(this.tradeInventory);
		}
	}

	/**
	 * Setups the inventory, adds Title, Borders etc.
	 * @param p Player that was requested
	 * @param requester The requesting player.
	 */
	public void setupInventory(Player p, Player requester) {
		if (this.tradeInventory == null) {
			Player player1 = this.getPlayer1(), player2 = this.getPlayer2();
			
			this.tradeInventory = Bukkit.getServer().createInventory(null, 54, "Trading Inventory");
			
		} else {
			this.tradeInventory.clear();
		}
		
		/*
		 * Creates a new ItemBuilder, custom class for creating items.
		 */
		
		ItemBuilder glassSeparator = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getData());
		
		glassSeparator.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c" + p.getName() + " &c< &c> " + requester.getName()));
		
		ItemBuilder readyItem = new ItemBuilder(Material.WOOL, 1, DyeColor.RED.getData());
		
		readyItem.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClick to trade."));
		
		for (int i = 0; i < 6; i++) {
			try {
				this.tradeInventory.setItem((9 * (i + 1) - 5), glassSeparator.getItem());
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
		}
		
		
		ItemBuilder redSeparator = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getData());
		
		redSeparator.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cClick when ready."));
		
		for (int i = 0; i < 9; i++) {
			if (i < 3 || i > 5) {
				this.tradeInventory.setItem(i, redSeparator.getItem());
			}
		}
		
		this.tradeInventory.setItem(3, readyItem.getItem());
		this.tradeInventory.setItem(5, readyItem.getItem());

		this.player1Slots = new ArrayList<Integer>();
		this.player2Slots = new ArrayList<Integer>();

		for (int i = 0; i < this.tradeInventory.getSize(); i++) {
			if (i > 8 && i % 9 != 4) {
				if (this.canClick(1, i)) {
					this.player1Slots.add(i);
				} else {
					this.player2Slots.add(i);
				}
			}
		}
	}
	
	

	@EventHandler
	public void onPlayerClick(InventoryClickEvent event) {
		try {
			if (event.getWhoClicked() instanceof Player && this.tradeInventory != null) {
				
				Player player = (Player) event.getWhoClicked();
				
				int playerType = player.getUniqueId().equals(this.player1) ? 1 : (player.getUniqueId().equals(this.player2) ? 2 : 0);
				
				
				if (playerType != 0) {
					if (!(event.getRawSlot() >= 0 && event.getRawSlot() < this.tradeInventory.getSize() + player.getInventory().getSize())) {
						event.setCancelled(true);
						return;
					}
					
					
					Player player1 = this.getPlayer1(), player2 = this.getPlayer2();
					
					
					if (event.getRawSlot() < this.tradeInventory.getSize()) {
						if (this.canClickItem(playerType, event.getRawSlot())) {
							
							if (playerType == 1){
								this.confirmed1 = !this.confirmed1;
							}else{
								this.confirmed2 = !this.confirmed2;
							}

							if (playerType == 1 ? this.confirmed1 : this.confirmed2) {
								ItemBuilder itemSeparator = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getData());
								
								itemSeparator.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&AAccepted Trade."));
								
								for (int i = 1; i < 4; i++) {
									if (playerType == 1) {
										this.tradeInventory.setItem(event.getSlot() - i, itemSeparator.getItem());
									} else {
										this.tradeInventory.setItem(event.getSlot() + i, itemSeparator.getItem());
									}
								}
								
								ItemBuilder tradeAccept = new ItemBuilder(Material.WOOL, 1, DyeColor.LIME.getData());
								
								tradeAccept.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&AAccepted Trade"));
								
								this.tradeInventory.setItem(event.getSlot(), tradeAccept.getItem());
							
							} else {
								ItemBuilder itemSeparator = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getData());
								
								itemSeparator.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cNot Ready"));
								
								for (int i = 1; i < 4; i++) {
									if (playerType == 1) {
										this.tradeInventory.setItem(event.getSlot() - i, itemSeparator.getItem());
									} else {
										this.tradeInventory.setItem(event.getSlot() + i, itemSeparator.getItem());
									}
								}
								
								ItemBuilder tradeDeny = new ItemBuilder(Material.WOOL, 1, DyeColor.RED.getData());
								
								tradeDeny.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cNot Ready"));
								
								this.tradeInventory.setItem(event.getSlot(), tradeDeny.getItem());
							}

							if (this.confirmed1 && this.confirmed2) {
								
								ItemStack[] items1 = this.getItems(1);
								ItemStack[] items2 = this.getItems(2);
								

								if (player1 != null && items2.length > 0) {
									
									Map<Integer, ItemStack> droppedItems = player1.getInventory().addItem(items2);
									
									if (droppedItems != null) {
										for (ItemStack droppedItem : droppedItems.values())
											
											player1.getWorld().dropItemNaturally(player1.getLocation(), droppedItem);
									}
								}
								if (player2 != null && items1.length > 0) {
									
									Map<Integer, ItemStack> droppedItems = player2.getInventory().addItem(items1);
									
									if (droppedItems != null) {
										
										for (ItemStack droppedItem : droppedItems.values())
											player2.getWorld().dropItemNaturally(player1.getLocation(), droppedItem);
									}
								}

								event.setCancelled(true);
								this.closeMenu(true, true);
								return;
							}
						} else {
							if (this.canClick(playerType, event.getRawSlot())) {
								
								ItemStack clickedItem = event.getCurrentItem();
								
								if (clickedItem != null && clickedItem.getType() != Material.AIR) {
									
									Map<Integer, ItemStack> droppedItems = player.getInventory().addItem(clickedItem);
									
									if (droppedItems != null && !droppedItems.isEmpty()) {
										for (ItemStack droppedItem : droppedItems.values())
											player.getWorld().dropItemNaturally(player.getLocation(), droppedItem);
									}
								}
								this.tradeInventory.setItem(event.getRawSlot(), new ItemStack(Material.AIR));
							}
						}
					} else {
						if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
							if (this.addItem(playerType, event.getCurrentItem())) {
								player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
							}
						}
					}

					event.setCancelled(true);
					player1.updateInventory();
					player2.updateInventory();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void onPlayerCloseInventory(InventoryCloseEvent event) {
		try {
			if (event.getPlayer() instanceof Player) {
				
				int playerType = event.getPlayer().getUniqueId().equals(this.player1) ? 1 : (event.getPlayer().getUniqueId().equals(this.player2) ? 2 : 0);
				
				if (playerType != 0) {
					
					Player player1 = this.getPlayer1();
					
					Player player2 = this.getPlayer2();
					
					ItemStack[] addItems1 = this.getItems(1);
					ItemStack[] addItems2 = this.getItems(2);
					
					if (player1 != null) {
						Map<Integer, ItemStack> droppedItems = player1.getInventory().addItem(addItems1);
						if (droppedItems != null && !droppedItems.isEmpty()) {
							for (ItemStack droppedItem : droppedItems.values())
								player1.getWorld().dropItemNaturally(player1.getLocation(), droppedItem);
								player1.updateInventory();
						}

						if (playerType == 2){
							player1.sendMessage(ChatColor.RED + event.getPlayer().getName() + " cancelled the trade.");
						}
						else {
							player1.sendMessage(ChatColor.RED + "You cancelled the trade.");
						}
					}
					if (player2 != null) {
						Map<Integer, ItemStack> droppedItems = player2.getInventory().addItem(addItems2);
						if (droppedItems != null && !droppedItems.isEmpty()) {
							for (ItemStack droppedItem : droppedItems.values())
								
							player2.getWorld().dropItemNaturally(player2.getLocation(), droppedItem);
								
							player2.updateInventory();
						}

						if (playerType == 1){
							player2.sendMessage(ChatColor.RED + event.getPlayer().getName() + " cancelled the trade.");
						}
						else {
							player2.sendMessage(ChatColor.RED + "You cancelled the trade.");
						}
					}

					if (this.tradeInventory != null) this.tradeInventory.clear();
					this.closeMenu(true, playerType == 2 ? true : false, playerType == 1 ? true : false);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
