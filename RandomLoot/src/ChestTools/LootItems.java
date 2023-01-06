package ChestTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class LootItems {

	private Material material;
	private String displayName;
	private HashMap<Enchantment, Integer> enchants = new HashMap<>();
	private double chance;
	private int maxamount;
	private int minamount;
	private ArrayList<String> lore;

	@SuppressWarnings("unchecked")
	public LootItems(ConfigurationSection cfg) {
		try {
			material = Material.getMaterial(cfg.getString("material").toUpperCase());
		} catch (Exception e) {
			material = Material.AIR;
		}
		displayName = cfg.getString("name");
		ConfigurationSection enchsection = cfg.getConfigurationSection("enchants");
		if (enchsection != null) {
			for (String enchants : enchsection.getKeys(false)) {
				Enchantment ench = Enchantment.getByName(enchants);
				if (ench != null) {
					int level = cfg.getInt(enchants);
					this.enchants.put(ench, level);
				}
			}
		}
		if(!cfg.getList("lore").isEmpty()) {
			ArrayList<String> oldlore = (ArrayList<String>) cfg.getList("lore");
			for(String o:oldlore) {
				lore.add(ChatColor.translateAlternateColorCodes('&', o));
			}
			
		}

		
		chance = cfg.getDouble("chance");
		maxamount = cfg.getInt("maxAmount");
		minamount = cfg.getInt("minAmount");
	}
	
	public boolean shouldBeAdded(Random random) {
		return random.nextDouble() <chance;
	}
	
	public ItemStack CreateItem(ThreadLocalRandom random) {
		int amount = random.nextInt(minamount, maxamount+1);
		ItemStack item = new ItemStack(material,amount);
		ItemMeta meta = item.getItemMeta();
		if(displayName !=null) {
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		}
		if(!enchants.isEmpty()) {
			for(Entry<Enchantment, Integer> ench : enchants.entrySet()) {
				meta.addEnchant(ench.getKey(), ench.getValue(),true);
			}
		}
		if(!lore.isEmpty()) {
			meta.setLore(lore);
		}
		
		item.setItemMeta(meta);
		
		
		
		return item;
	}
	
	
	

}
