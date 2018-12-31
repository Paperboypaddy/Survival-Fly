package me.paperboypaddy16.SurvivalFly.joinevent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.paperboypaddy16.SurvivalFly.Main;
import me.paperboypaddy16.SurvivalFly.commands.FlyCmd;

public class JoinItem implements Listener {
	Main plugin = Main.getPlugin(Main.class);
	private FlyCmd Flycmds;

	public JoinItem(FlyCmd fl) {
		Flycmds = fl;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		Boolean imena = plugin.getConfig().getBoolean("Enable JoinItem");
		String Item = plugin.getConfig().getString("Join Item");
		org.bukkit.inventory.PlayerInventory inv = player.getInventory();
		ItemStack FlyItem = new ItemStack(Material.getMaterial(Item));
		inv.clear();
		inv.addItem(FlyItem);
		if (player.isOp()) {
			if (plugin.Old) {
				player.sendMessage(
						ChatColor.GOLD + "There is a new update available at www.spigotmc.org/resources/62068/");
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		Boolean imena = plugin.getConfig().getBoolean("Enable JoinItem");
		String Item = plugin.getConfig().getString("Join Item");
		int cdtime = plugin.getConfig().getInt(uuid + ".Cooldown_Left");
		plugin.cdtime.put(uuid, cdtime);
		if (imena) {
			org.bukkit.inventory.PlayerInventory inv = player.getInventory();
			ItemStack FlyItem = new ItemStack(Material.getMaterial(Item));
			inv.clear();
			inv.addItem(FlyItem);
		}
	}

	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		plugin.cdtime.remove(uuid);

	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFlyToggleRightClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		String Item = plugin.getConfig().getString("Join Item");
		if ((!plugin.cdtime.containsKey(uuid))&&(e.getAction() == Action.RIGHT_CLICK_AIR||e.getAction() == Action.RIGHT_CLICK_BLOCK)&&(p.getItemInHand().getType() == Material.getMaterial(Item))){
			FlyCmd.ToggleFly(e.getPlayer());
			plugin.cdtime.put(uuid, plugin.mastercd);
		} 
	}
}