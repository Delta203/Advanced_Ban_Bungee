package de.ab.delta203.bungee.listeners;

import de.ab.delta203.bungee.files.ChatFilterYML;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQL_ChatLog;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Mute;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Listener_ChatLog implements Listener {

	@EventHandler
	public void onChat(ChatEvent e) {
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) e.getSender();
			String msg = e.getMessage().replace("<", "&#60;").replace(">", "&#62;");
			if(e.getMessage().startsWith("/")) {
				String formatedMsg = MessagesYML.get().getString("cl_command").replace("%command%", msg);
				GetMySQL_ChatLog.record(p.getUniqueId().toString(), p.getName(), p.getServer().getInfo().getName(), formatedMsg);
			}else {
				if(!GetMySQl_Mute.isMuted(p.getUniqueId().toString())) {
					GetMySQL_ChatLog.record(p.getUniqueId().toString(), p.getName(), p.getServer().getInfo().getName(), msg);
					
					/* Chat blacklist check */
					for(String blackwords : ChatFilterYML.get().getStringList("blacklist")) {
						if(e.getMessage().contains(blackwords)) {
							BungeeCord.getInstance().pluginManager.dispatchCommand(BungeeCord.getInstance().getConsole(), "report " + p.getName() + " " + MessagesYML.get().getString("report_chatfilter"));
							BungeeCord.getInstance().pluginManager.dispatchCommand(BungeeCord.getInstance().getConsole(), "report $confirm");
							return;
						}
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void onSwitch(ServerSwitchEvent e) {
		ProxiedPlayer p = e.getPlayer();
		if(e.getFrom() != null) GetMySQL_ChatLog.record(p.getUniqueId().toString(), p.getName(), e.getFrom().getName(), MessagesYML.get().getString("cl_player_disconnect").replace("%player%", p.getName()));
		GetMySQL_ChatLog.record(p.getUniqueId().toString(), p.getName(), p.getServer().getInfo().getName(), MessagesYML.get().getString("cl_player_connect").replace("%player%", p.getName()));
	}
	
	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		GetMySQL_ChatLog.record(p.getUniqueId().toString(), p.getName(), p.getServer().getInfo().getName(), MessagesYML.get().getString("cl_player_disconnect").replace("%player%", p.getName()));
	}
}
