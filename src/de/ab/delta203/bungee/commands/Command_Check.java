package de.ab.delta203.bungee.commands;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Ban;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Mute;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerInfo;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Command_Check extends Command {

	public Command_Check() {
		super("check");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("ab.check")) {
			if(args.length == 1) {
				String targetUUID = GetMySQl_PlayerInfo.getUUIDByName(args[0]); 
				if(targetUUID != null) {
					String targetName = GetMySQl_PlayerInfo.getNameByUUID(targetUUID);
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("check.title").replace("%player%", targetName));
					
					if(BungeeCord.getInstance().getPlayer(targetName) != null) { 
						sender.sendMessage(MessagesYML.get().getString("check.is_online_yes")
								.replace("%server%", BungeeCord.getInstance().getPlayer(targetName).getServer().getInfo().getName()));
					}else {
						sender.sendMessage(MessagesYML.get().getString("check.is_online_no"));
					}
					
					if(GetMySQl_Ban.isBannedUUID(targetUUID)) {
						sender.sendMessage(MessagesYML.get().getString("check.banned_yes"));
						
						if(!GetMySQl_Ban.getFrom(targetUUID).equals("Console")) {
							sender.sendMessage(MessagesYML.get().getString("check.banned_from").replace("%name%", GetMySQl_PlayerInfo.getNameByUUID(GetMySQl_Ban.getFrom(targetUUID))));
						}else {
							sender.sendMessage(MessagesYML.get().getString("check.banned_from").replace("%name%", "Console"));
						}
						sender.sendMessage(MessagesYML.get().getString("check.banned_reason").replace("%reason%", GetMySQl_Ban.getReason(targetUUID)));
						sender.sendMessage(MessagesYML.get().getString("check.banned_duration").replace("%duration%", GetMySQl_Ban.getEndString(targetUUID)));
					}else {
						sender.sendMessage(MessagesYML.get().getString("check.banned_no"));
					}
					if(GetMySQl_Mute.isMuted(targetUUID)) {
						sender.sendMessage(MessagesYML.get().getString("check.muted_yes"));
						if(!GetMySQl_Mute.getFrom(targetUUID).equals("Console")) {
							sender.sendMessage(MessagesYML.get().getString("check.muted_from").replace("%name%", GetMySQl_PlayerInfo.getNameByUUID(GetMySQl_Mute.getFrom(targetUUID))));
						}else {
							sender.sendMessage(MessagesYML.get().getString("check.muted_from").replace("%name%", "Console"));
						}
						sender.sendMessage(MessagesYML.get().getString("check.muted_reason").replace("%reason%", GetMySQl_Mute.getReason(targetUUID)));
						sender.sendMessage(MessagesYML.get().getString("check.muted_duration").replace("%duration%", GetMySQl_Mute.getEndString(targetUUID)));
					}else {
						sender.sendMessage(MessagesYML.get().getString("check.muted_no"));
					}
				}else {
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("player_not_registered").replace("%player%", args[0]));
				}
			}else {
				AdvancedBanMain.sendBanHelp(sender, 7);
			}
		}else {
			sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("no_perm"));
		}
	}
}
