package util;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	ItemStack item;

	public ItemBuilder(Material item, Integer i) {
		this.item = new ItemStack(item, i);
	}
	
	public static ItemStack renameItem(ItemStack it, String name){
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		return it;
	}

	public ItemBuilder(Material hardClay, int i, byte woolData) {
		this.item = new ItemStack(hardClay, i, woolData);
	}    

	public ItemStack getItem() {
		return item;
	}

	public void setItem(Material item, Integer i, byte woolData) {
		this.item = new ItemStack(item, i, woolData);
	}

	public void setItem(Material item, Integer i){
		this.item = new ItemStack(item, i);
	}
	
	public void setLore(List<String> s) {
		ItemMeta im = item.getItemMeta();
		im.setLore(s);

		item.setItemMeta(im);

	}

	public void setDisplayName(String s) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(s);

		item.setItemMeta(im);
	}
}
