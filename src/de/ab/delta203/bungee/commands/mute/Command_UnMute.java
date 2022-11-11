package de.ab.delta203.bungee.commands.mute;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.ConfigYML;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Mute;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerHistory;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerInfo;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_UnMute extends Command {

	public Command_UnMute() {
		super("unmute");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("ab.unmute")) {
			if(args.length == 1) {
				String targetUUID = GetMySQl_PlayerInfo.getUUIDByName(args[0]); 
				if(targetUUID != null) {
					if(GetMySQl_Mute.isMuted(targetUUID)) {
						String senderName = "Console";
						if(sender instanceof ProxiedPlayer) senderName = ((ProxiedPlayer) sender).getName();
						
						GetMySQl_Mute.unmute(targetUUID);
						GetMySQl_PlayerHistory.insert(targetUUID, senderName, "Unmute", "-", "-");
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("unmute_success.sender").replace("%player%", args[0]));
						if(ConfigYML.get().getBoolean("notify_mute")) {
							for(ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
								if(team.hasPermission("ab.mute") || team.hasPermission("ab.tempmute")) {
									team.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("unmute_success.all").replace("%player%", args[0]).replace("%from%", senderName));
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
				AdvancedBanMain.sendBanHelp(sender, 6);
			}
		}else {
			sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("no_perm"));
		}
	}
}