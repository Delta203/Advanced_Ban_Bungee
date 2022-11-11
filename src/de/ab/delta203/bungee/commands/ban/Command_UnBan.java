package de.ab.delta203.bungee.commands.ban;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.ConfigYML;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Ban;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerHistory;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerInfo;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_UnBan extends Command {

	public Command_UnBan() {
		super("unban");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("ab.unban")) {
			if(args.length == 1) {
				String targetUUID = GetMySQl_PlayerInfo.getUUIDByName(args[0]); 
				if(targetUUID != null) {
					if(GetMySQl_Ban.isBannedUUID(targetUUID)) {
						String senderName = "Console";
						if(sender instanceof ProxiedPlayer) senderName = ((ProxiedPlayer) sender).getName();
						
						GetMySQl_Ban.unban(targetUUID);
						GetMySQl_PlayerHistory.insert(targetUUID, senderName, "Unban", "-", "-");
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("unban_success.sender").replace("%player%", args[0]));
						if(ConfigYML.get().getBoolean("notify_ban")) {
							for(ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
								if(team.hasPermission("ab.ban") || team.hasPermission("ab.tempban")) {
									team.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("unban_success.all").replace("%player%", args[0]).replace("%from%", senderName));
								}
							}
						}
					}else {
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("bm_target_is_not_banned_muted").replace("%player%", args[0]));
					}
				}else {
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("player_not_registered").replace("%player%", args[0]));
				}
			}else {
				AdvancedBanMain.sendBanHelp(sender, 5);
			}
		}else {
			sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("no_perm"));
		}
	}
}