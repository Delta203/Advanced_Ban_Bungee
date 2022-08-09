package de.ab.delta203.bungee;

import java.util.concurrent.TimeUnit;

import de.ab.delta203.bungee.commands.Command_ABPanel;
import de.ab.delta203.bungee.commands.Command_Check;
import de.ab.delta203.bungee.commands.Command_Report;
import de.ab.delta203.bungee.commands.ban.Command_Ban;
import de.ab.delta203.bungee.commands.ban.Command_BanTemp;
import de.ab.delta203.bungee.commands.ban.Command_UnBan;
import de.ab.delta203.bungee.commands.mute.Command_Mute;
import de.ab.delta203.bungee.commands.mute.Command_MuteTemp;
import de.ab.delta203.bungee.commands.mute.Command_UnMute;
import de.ab.delta203.bungee.files.ChatFilterYML;
import de.ab.delta203.bungee.files.ConfigYML;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.listeners.Listener_Ban;
import de.ab.delta203.bungee.listeners.Listener_ChatLog;
import de.ab.delta203.bungee.listeners.Listener_Mute;
import de.ab.delta203.bungee.listeners.Listener_Register;
import de.ab.delta203.bungee.mysql.MySQl;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_CommandQuery;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

public class AdvancedBanMain extends Plugin {

	public static AdvancedBanMain plugin;
	public static String prefix;
	public static MySQl mysql;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		plugin = this;
		ConfigYML.create();
		ChatFilterYML.create();
		MessagesYML.create();
		prefix = MessagesYML.get().getString("prefix");
		BungeeCord.getInstance().getConsole().sendMessage(prefix + MessagesYML.get().getString("load_plugin"));
		BungeeCord.getInstance().getConsole().sendMessage(prefix + MessagesYML.get().getString("register_configs"));
		
		mysql = new MySQl(ConfigYML.get().getString("MySQl_url"),
				ConfigYML.get().getInt("MySQl_port"),
				ConfigYML.get().getString("MySQl_database"),
				ConfigYML.get().getString("MySQl_user"),
				ConfigYML.get().getString("MySQl_password"));
		mysql.connect();
		mysql.createTables();
		
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_Ban());
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_BanTemp());
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_UnBan());
		
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_Mute());
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_MuteTemp());
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_UnMute());
		
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_ABPanel());
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_Check());
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new Command_Report());
		
		BungeeCord.getInstance().getPluginManager().registerListener(this, new Listener_ChatLog());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new Listener_Ban());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new Listener_Mute());
		BungeeCord.getInstance().getPluginManager().registerListener(this, new Listener_Register());
		
		BungeeCord.getInstance().getConsole().sendMessage(prefix + MessagesYML.get().getString("loaded_plugin"));
		
		if(ConfigYML.get().getBoolean("command_query.enabled")) runCommandQuery();
	}
	
	@SuppressWarnings("deprecation")
	private void runCommandQuery() {
		BungeeCord.getInstance().getConsole().sendMessage(prefix + MessagesYML.get().getString("command_query_started"));
		BungeeCord.getInstance().getScheduler().schedule(this, new Runnable() {
			
			@Override
			public void run() {
				GetMySQl_CommandQuery.fetchCommands();
			}
		}, 0, ConfigYML.get().getInt("command_query.delay"), TimeUnit.SECONDS);
	}

	@SuppressWarnings("deprecation")
	public static void sendReportHelp(CommandSender sender) {
		sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_help.title"));
		sender.sendMessage(MessagesYML.get().getString("report_help.1"));
		sender.sendMessage(MessagesYML.get().getString("report_help.2"));
	}
	
	@SuppressWarnings("deprecation")
	public static void sendBanHelp(CommandSender sender, int id) {
		sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("bm_help.title"));
		if(id == 0) {
			if(sender.hasPermission("ab.ban")) sender.sendMessage(MessagesYML.get().getString("bm_help.1"));
			if(sender.hasPermission("ab.tempban")) sender.sendMessage(MessagesYML.get().getString("bm_help.2"));
			if(sender.hasPermission("ab.mute"))sender.sendMessage(MessagesYML.get().getString("bm_help.3"));
			if(sender.hasPermission("ab.tempmute")) sender.sendMessage(MessagesYML.get().getString("bm_help.4"));
			if(sender.hasPermission("ab.unban")) sender.sendMessage(MessagesYML.get().getString("bm_help.5"));
			if(sender.hasPermission("ab.unmute")) sender.sendMessage(MessagesYML.get().getString("bm_help.6"));
			if(sender.hasPermission("ab.check")) sender.sendMessage(MessagesYML.get().getString("bm_help.7"));
			if(sender.hasPermission("ab.panel")) sender.sendMessage(MessagesYML.get().getString("bm_help.8"));
		}else {
			sender.sendMessage(MessagesYML.get().getString("bm_help." + id));
		}
	}
}
