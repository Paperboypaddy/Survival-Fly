package me.paperboypaddy16.SurvivalFly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SurvivalFly extends JavaPlugin {

	public HashMap<UUID, Integer> cdtime = new HashMap<UUID, Integer>();
	public int mastercd = 1;
	public Permission use = new Permission("survivalfly.use");
	public Permission help = new Permission("survivalfly.help");
	public Permission rl = new Permission("survivalfly.reload");
	public Permission ALL = new Permission("survivalfly.");
	public boolean Old;

	@Override
	public void onEnable() {
		runablerunner();
		VersionChecker();
		getLogger().info("Enabled");
		RegisterEvents();

	}

	private void RegisterEvents() {
		getCommand("fly").setExecutor(new FlyCmd());
		PluginManager pm = Bukkit.getPluginManager();
		pm.addPermission(use);
		pm.addPermission(help);
		pm.addPermission(rl);
		pm.addPermission(ALL);
		pm.registerEvents(new FlyCmd(), this);
		pm.registerEvents(new JoinItem(null), this);
		registerConfig();
	}

	private void registerConfig() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}

	public void runablerunner() {
		new BukkitRunnable() {

			@Override
			public void run() {

				if (cdtime.isEmpty())
					return;
				for (UUID uuid : cdtime.keySet()) {
					int timeleft = cdtime.get(uuid);
					if (!(timeleft < 1)) {
						cdtime.remove(uuid);
						cdtime.put(uuid, timeleft - 1);
					} else {
						cdtime.remove(uuid);
					}

				}
			}

		}.runTaskTimer(this, 0, 4);
	}

	private void VersionChecker() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(
					"https://api.spigotmc.org/legacy/update.php?resource=62068").openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
			if (!version.equals("v0.4")) {
				this.getServer().getConsoleSender().sendMessage(
						ChatColor.YELLOW + "There is a new update available at www.spigotmc.org/resources/62068/");
				Old = true;
			} else {
				this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Survival-Fly is upto date");
				Old = false;
			}
		} catch (IOException e) {
			this.getServer().getConsoleSender()
					.sendMessage("[SurvivalFly]" + ChatColor.RED + " ERROR: COULD NOT CHECK FOR UPDATE");
			this.getServer().getConsoleSender()
					.sendMessage("[SurvivalFly]" + ChatColor.RED + " please check your servers internet connection");
		}
	}

}