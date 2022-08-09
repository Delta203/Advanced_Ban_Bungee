package de.ab.delta203.bungee.commands.ban;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.ConfigYML;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Ban;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Mute;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerHistory;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerInfo;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_Ban extends Command {

	public Command_Ban() {
		super("ban");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("ab.ban")) {
			if(args.length >= 2) {
				String targetUUID = GetMySQl_PlayerInfo.getUUIDByName(args[0]); 
				if(targetUUID != null) {
					if(!GetMySQl_Ban.isBannedUUID(targetUUID)) {
						String reason = "";
						for(int i = 1; i < args.length; i++) {
							reason += args[i] + " ";
						}
						String senderUUID = "Console";
						String senderName = "Console";
						if(sender instanceof ProxiedPlayer) { 
							senderUUID = ((ProxiedPlayer) sender).getUniqueId().toString();
							senderName = ((ProxiedPlayer) sender).getName();
						}
						String targetIP = "-";
						ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
						if(target != null) {
							targetIP = target.getAddress().getAddress().toString();
							target.disconnect(MessagesYML.get().getString("you_are_banned.kick")
									.replace("$n", "\n")
									.replace("%reason%", reason));
						}
						
						GetMySQl_Ban.ban(targetUUID, targetIP, senderUUID, reason, -1);
						GetMySQl_PlayerHistory.insert(targetUUID, senderUUID, "Ban", reason, "-");
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("ban_success.sender").replace("%player%", args[0]));
						if(ConfigYML.get().getBoolean("notify_ban")) {
							for(ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
								if(team.hasPermission("ab.ban") || team.hasPermission("ab.tempban")) {
									team.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("ban_success.all").replace("%player%", args[0]).replace("%from%", senderName).replace("%duration%", GetMySQl_Mute.getEndString(targetUUID)));
								}
							}
						}
					}else {
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("bm_target_already_banned_muted").replace("%player%", args[0]));
					}
				}else {
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("player_not_registered").replace("%player%", args[0]));
				}
			}else {
				AdvancedBanMain.sendBanHelp(sender, 0);
			}
		}else {
			sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("no_perm"));
		}
	}
}
