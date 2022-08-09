package de.ab.delta203.bungee.listeners;

import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Ban;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Listener_Ban implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(LoginEvent e) {
		String uuid = e.getConnection().getUniqueId().toString();
		String ip = e.getConnection().getAddress().getAddress().toString();
		
		long current = System.currentTimeMillis();
		long end = GetMySQl_Ban.getEnd(uuid);
		if(current < end || end == -1) {
			if(GetMySQl_Ban.isBannedUUID(uuid)) {
				//e.setCancelReason("Warum geht das \n nicht");
				e.setCancelReason(MessagesYML.get().getString("you_are_banned.login")
						.replace("$n", "\n")
						.replace("%reason%", GetMySQl_Ban.getReason(uuid))
						.replace("%duration%", GetMySQl_Ban.getEndString(uuid)));
				e.setCancelled(true);
				return;
			}
		}else {
			GetMySQl_Ban.unban(uuid);
		}
		
		if(GetMySQl_Ban.isBannedIP(ip)) {
			e.setCancelReason(MessagesYML.get().getString("you_are_banned.login_ipban")
					.replace("$n", "\n"));
			e.setCancelled(true);
			return;
		}
	}
}
