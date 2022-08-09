package de.ab.delta203.bungee.listeners;

import java.util.UUID;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.ConfigYML;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerInfo;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Report;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Listener_Register implements Listener {

	@EventHandler
	public void onLogin(LoginEvent e) {
		if(!GetMySQl_PlayerInfo.isRegistered(e.getConnection().getUniqueId().toString())) {
			GetMySQl_PlayerInfo.register(e.getConnection().getUniqueId().toString(), e.getConnection().getName());
		}
		if(!GetMySQl_PlayerInfo.getNameByUUID(e.getConnection().getUniqueId().toString()).equals(e.getConnection().getName())) {
			GetMySQl_PlayerInfo.updateName(e.getConnection().getUniqueId().toString(), e.getConnection().getName());
		}
		GetMySQl_PlayerInfo.updateLoginKey(e.getConnection().getUniqueId().toString(), UUID.randomUUID().toString().replace("-", ""));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSwitch(ServerSwitchEvent e) {
		ProxiedPlayer p = e.getPlayer();
		GetMySQl_PlayerInfo.updateServer(p.getUniqueId().toString(), p.getServer().getInfo().getName());
		
		/* report notification */
		if(GetMySQl_Report.getReportsCount() > 0) {
			if(ConfigYML.get().getBoolean("notify_report")) {
				if(p.hasPermission("ab.panel")) {
					p.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_notify_others").replace("%reportscount%", String.valueOf(GetMySQl_Report.getReportsCount())));
				}
			}
		}
	}
	
	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		GetMySQl_PlayerInfo.updateServer(p.getUniqueId().toString(), "-");
		GetMySQl_PlayerInfo.updateLoginKey(p.getUniqueId().toString(), "-");
	}
}
