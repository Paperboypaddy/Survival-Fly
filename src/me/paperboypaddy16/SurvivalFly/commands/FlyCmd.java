package me.paperboypaddy16.SurvivalFly.commands;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import me.paperboypaddy16.SurvivalFly.Main;
import me.paperboypaddy16.SurvivalFly.joinevent.JoinItem;
import net.minecraft.server.v1_12_R1.CommandExecute;

public class FlyCmd extends CommandExecute implements Listener, CommandExecutor {

	private void RegisterEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new JoinItem(this), plugin);
	}

	static Main plugin = Main.getPlugin(Main.class);

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String[] helplist = { ChatColor.AQUA + "-----------SurvivalFly--Help------------",
				ChatColor.AQUA + "/fly " + ChatColor.YELLOW + "Toggles FlyModes",
				ChatColor.AQUA + "/fly admin " + ChatColor.YELLOW + "Shows Admin help page",
				ChatColor.AQUA + "/fly reload " + ChatColor.YELLOW + "Reloads the config file",
				ChatColor.AQUA + "/fly help " + ChatColor.YELLOW + "Shows this page",
				ChatColor.AQUA + "--------------------------------------" };
		String[] adminhelplist = { ChatColor.AQUA + "--------SurvivalFly--Admin--Help---------",
				ChatColor.AQUA + "/fly admin [toggle|tg] {PLAYER} " + ChatColor.YELLOW
						+ "Toggles FlyModes of Specified Player",
				ChatColor.AQUA + "/fly admin [disable|dis] {PLAYER} " + ChatColor.YELLOW
						+ "Disables Fly of Specified Player",
				ChatColor.AQUA + "/fly admin [enable|en] {PLAYER} " + ChatColor.YELLOW
						+ "Enables Fly of Specified Player",
				ChatColor.AQUA + "/fly admin help " + ChatColor.YELLOW + "Shows this page",
				ChatColor.AQUA + "--------------------------------------" };
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		String notacmd = plugin.getConfig().getString("Not a Cmd message");
		String notaadmincmd = plugin.getConfig().getString("Not a Admin Cmd message");
		String Enabled = plugin.getConfig().getString("Enabled Fly message");
		String Disabled = plugin.getConfig().getString("Disabled Fly message");
		String noPermission = plugin.getConfig().getString("No Permission message");
		Boolean FlycmdPermB = plugin.getConfig().getBoolean("Require Permission for Fly Cmd");
		Boolean ReloadcmdPermB = plugin.getConfig().getBoolean("Require Permission for Reload Cmd");
		Boolean HelpcmdPermB = plugin.getConfig().getBoolean("Require Permission for Help Cmd");
		String FlyCmdPerm = plugin.getConfig().getString("FlyCmd Permisson");
		String ReloadCmdPerm = plugin.getConfig().getString("ReloadCmd Permisson");
		String HelpCmdPerm = plugin.getConfig().getString("HelpCmd Permission");
		String AdminCmdsPerm = plugin.getConfig().getString("AdminCmds Permisson");

		// FLY Command:
		if (cmd.getName().equalsIgnoreCase("fly")) {

			if (args.length == 0) {
				if (FlycmdPermB) {
					if (!player.hasPermission(FlyCmdPerm)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));
						return true;
					}
				}
				if (player.hasPermission("survivalfly.use")) {
					List<String> disworlds = plugin.getConfig().getStringList("Disabled Worlds");
					Boolean discon = plugin.getConfig().getBoolean("Enable disabeled worlds");
					if (discon) {
						if (!disworlds.contains(player.getWorld().getName())) {
							ToggleFly(sender);
						}
					} else {
						ToggleFly(sender);
					}
				}

				// FLY RELOAD Command:
			} else if (args[0].equalsIgnoreCase("reload")) {
				if (ReloadcmdPermB) {
					if (!player.hasPermission(ReloadCmdPerm)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));
						return true;
					}
				}
				plugin.reloadConfig();
				player.sendMessage("Config.yml Reloaded");

				// FLY HELP Command:
			} else if (args[0].equalsIgnoreCase("help")) {
				if (HelpcmdPermB) {
					if (!player.hasPermission(HelpCmdPerm)) {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));
						return true;
					}
				}
				player.sendMessage(helplist);

				// FLY ADMIN Commands:
			} else if (args[0].equalsIgnoreCase("admin")) {

				if (player.hasPermission("survivalfly.admin")) {

					if (args.length >= 2) {

						// FLY ADMIN HELP Command:
						if (args[1].equalsIgnoreCase("help")) {
							player.sendMessage(adminhelplist);

							// FLY ADMIN TOGGLE Command:
						} else if (args[1].equalsIgnoreCase("toggle") || args[1].equalsIgnoreCase("tg")) {
							if (args.length == 3) {
								Player target = Bukkit.getServer().getPlayer(args[2]);
								if (target == null) {
									player.sendMessage(ChatColor.RED + "That player wasnt found" + ChatColor.GRAY + "--"
											+ ChatColor.GOLD + "Please try again");
									player.sendMessage(ChatColor.RED + "Usage " + ChatColor.AQUA
											+ "/fly admin [toggle|tg] " + ChatColor.GREEN + "{PLAYER}");
								} else {
									ToggleFlyAdmin(sender, target);
								}
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', notaadmincmd));
							}

							// FLY ADMIN ENABLE Command:
						} else if (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("en")) {
							if (args.length == 3) {
								Player target = Bukkit.getServer().getPlayer(args[2]);
								if (target == null) {
									player.sendMessage(ChatColor.RED + "That player wasnt found" + ChatColor.GRAY + "--"
											+ ChatColor.GOLD + "Please try again");
									player.sendMessage(ChatColor.RED + "Usage " + ChatColor.AQUA
											+ "/fly admin [enable|en] " + ChatColor.GREEN + "{PLAYER}");
								} else {
									EnableFlyAdmin(sender, target);
								}
							} else {
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', notaadmincmd));
							}

							// FLY ADMIN DISABLE Command:
						} else if (args[1].equalsIgnoreCase("disable") || args[1].equalsIgnoreCase("dis")) {
							if (args.length == 3) {
								Player target = Bukkit.getServer().getPlayer(args[2]);
								if (target == null) {
									player.sendMessage(ChatColor.RED + "That player wasnt found" + ChatColor.GRAY + "--"
											+ ChatColor.GOLD + "Please try again");
									player.sendMessage(ChatColor.RED + "Usage " + ChatColor.AQUA
											+ "/fly admin [disable|dis] " + ChatColor.GREEN + "{PLAYER}");
								} else {
									DisableFlyAdmin(sender, target);
								}
							}
						} else {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', notaadmincmd));
						}

					} else {
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', notaadmincmd));
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermission));
					return true;
				}

			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', notacmd));
			}
		}
		return true;
	}

	public static void ToggleFly(CommandSender sender) {
		Player player = (Player) sender;
		String Enabled = plugin.getConfig().getString("Enabled Fly message");
		String Disabled = plugin.getConfig().getString("Disabled Fly message");
		if (player.getAllowFlight()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Disabled));
			player.setAllowFlight(false);
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Enabled));
			player.setAllowFlight(true);
		}
	}

	private void ToggleFlyAdmin(CommandSender sender, Player target) {
		Player player = (Player) sender;
		String Enabled = plugin.getConfig().getString("Enabled Other Fly message");
		String Disabled = plugin.getConfig().getString("Disabled Other Fly message");
		String Enabledthem = plugin.getConfig().getString("Got Enabled From Admin message");
		String Disablethem = plugin.getConfig().getString("Got Disabled From Admin message");
		if (target.getAllowFlight()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Disabled + target.getDisplayName()));
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', Disablethem));
			target.setAllowFlight(false);
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Enabled + target.getDisplayName()));
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', Enabledthem));
			target.setAllowFlight(true);
		}
	}

	private void EnableFlyAdmin(CommandSender sender, Player target) {
		Player player = (Player) sender;
		String Enabled = plugin.getConfig().getString("Enabled Other Fly message");
		String Enabledthem = plugin.getConfig().getString("Got Enabled From Admin message");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', Enabled + target.getDisplayName()));
		target.sendMessage(ChatColor.translateAlternateColorCodes('&', Enabledthem));
		target.setAllowFlight(true);
	}

	private void DisableFlyAdmin(CommandSender sender, Player target) {
		Player player = (Player) sender;
		String Disabled = plugin.getConfig().getString("Disabled Other Fly message");
		String Disablethem = plugin.getConfig().getString("Got Disabled From Admin message");
		if (target.getAllowFlight()) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Disabled + target.getDisplayName()));
			target.sendMessage(ChatColor.translateAlternateColorCodes('&', Disablethem));
			target.setAllowFlight(false);
		}
	}
}
