 package src;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ChestTools.ChestManager;

public class main extends JavaPlugin {

	
	private ChestManager chestmanager;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		chestmanager = new ChestManager(getConfig());
		super.onEnable();
		Bukkit.getPluginManager().registerEvents(chestmanager, this);
	}
	
}
