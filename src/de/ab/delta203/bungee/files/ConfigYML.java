package de.ab.delta203.bungee.files;

import java.io.File;
import java.io.IOException;

import de.ab.delta203.bungee.AdvancedBanMain;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigYML {
	
	private static File file = new File(AdvancedBanMain.plugin.getDataFolder(), "config.yml");
	private static Configuration cfg; 
	
	public static Configuration get() {
		return cfg;
	}

	public static void save() {
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void create() {
		if(!AdvancedBanMain.plugin.getDataFolder().exists()) {
			AdvancedBanMain.plugin.getDataFolder().mkdir();
		}
			
		try {
	        if(!file.exists()) {
	        	java.nio.file.Files.copy(AdvancedBanMain.plugin.getResourceAsStream("config.yml"), file.toPath());
	        	cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
	    	}else {
	    		cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
	    	}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
