package ChestTools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ChestManager implements Listener {
	
	
	private ArrayList<Location> openedChest = new ArrayList<>();
	private List<LootItems> loot  = new ArrayList<LootItems>();
	
	public ChestManager(FileConfiguration file) {
		ConfigurationSection cfg = file.getConfigurationSection("chest-items");
		if(cfg == null) {
			Bukkit.getConsoleSender().sendMessage("§cPlease setup your chest-item in your config.yml");
			return;
		}
		for(String key : cfg.getKeys(false)){
			ConfigurationSection item = cfg.getConfigurationSection(key);
			loot.add(new LootItems(item));
		}
		
	}
	
	@EventHandler
	public void onChestOpen(InventoryOpenEvent e) {
		InventoryHolder holder = e.getInventory().getHolder();
		if(holder instanceof Chest) {
			Chest chest =  (Chest) holder;
			if(isOpened(chest.getLocation()))return;
			
			setOpened(chest.getLocation());
			addItems(e.getInventory());
			
		}	
	}
	

	
	
	
	public void addItems(Inventory inv) {
		inv.clear();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		Set<LootItems> used = new HashSet<>();
		for(int slot=0;slot<inv.getSize();slot++) {
			LootItems item = loot.get(random.nextInt(loot.size()));
			if(used.contains(item))continue;
			used.add(item);
			
			if(item.shouldBeAdded(random)) {
				inv.setItem(slot, item.CreateItem(random));
			}
		}
		
		
		
	}
	
	public void reset() {
		openedChest.clear();
	}
	
	public void setOpened(Location lc) {
		openedChest.add(lc);
	}
	public boolean isOpened(Location lc) {
		return openedChest.contains(lc);
	}

}
