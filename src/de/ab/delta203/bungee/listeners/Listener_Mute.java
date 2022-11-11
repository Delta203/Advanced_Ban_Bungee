package de.ab.delta203.bungee.listeners;

import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Mute;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerHistory;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Listener_Mute implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent e) {
		if(e.getSender() instanceof ProxiedPlayer) {
			if(!e.getMessage().startsWith("/")) {
				ProxiedPlayer p = (ProxiedPlayer) e.getSender();
				if(GetMySQl_Mute.isMuted(p.getUniqueId().toString())) {
					long current = System.currentTimeMillis();
					long end = GetMySQl_Mute.getEnd(p.getUniqueId().toString());
					if(current < end || end == -1) {
						e.setCancelled(true);
						p.sendMessage(MessagesYML.get().getString("you_are_muted")
								.replace("%reason%", GetMySQl_Mute.getReason(p.getUniqueId().toString()))
								.replace("%duration%", GetMySQl_Mute.getEndString(p.getUniqueId().toString())));
					}else {
						GetMySQl_Mute.unmute(p.getUniqueId().toString());
						GetMySQl_PlayerHistory.insert(p.getUniqueId().toString(), "Console", "Unmute", "-", "-");
					}
				}
			}
		}
	}
}
