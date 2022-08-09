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

public class Command_MuteTemp extends Command {

	public Command_MuteTemp() {
		super("tempmute");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("ab.tempmute")) {
			if(args.length >= 4) {
				try {
					long value = Long.valueOf(args[1]);
					if(value <= ConfigYML.get().getInt("max_temp_value")) {
						String unit = args[2];
						if(unit.equalsIgnoreCase(MessagesYML.get().getString("BanUnit.seconds.alias"))) value = value * 1000;
						else if(unit.equalsIgnoreCase(MessagesYML.get().getString("BanUnit.minutes.alias"))) value = value * 60 * 1000;
						else if(unit.equalsIgnoreCase(MessagesYML.get().getString("BanUnit.hours.alias"))) value = value * 60 * 60 * 1000;
						else if(unit.equalsIgnoreCase(MessagesYML.get().getString("BanUnit.days.alias"))) value = value * 24 * 60 * 60 * 1000;
						else {
							sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("bm_wrong_unit"));
							return;
						}
						
						String targetUUID = GetMySQl_PlayerInfo.getUUIDByName(args[0]);
						if(targetUUID != null) {
							if(!GetMySQl_Mute.isMuted(targetUUID)) {
								String reason = "";
								for(int i = 3; i < args.length; i++) {
									reason += args[i] + " ";
								}
								String senderUUID = "Console";
								String senderName = "Console";
								if(sender instanceof ProxiedPlayer) { 
									senderUUID = ((ProxiedPlayer) sender).getUniqueId().toString();
									senderName = ((ProxiedPlayer) sender).getName();
								}
								
								GetMySQl_Mute.mute(targetUUID, senderUUID, reason, value + System.currentTimeMillis());
								GetMySQl_PlayerHistory.insert(targetUUID, senderUUID, "TempMute", reason, args[1] + " " + args[2]);
								sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("mute_success.sender").replace("%player%", args[0]));
								if(ConfigYML.get().getBoolean("notify_mute")) {
									for(ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
										if(team.hasPermission("ab.mute") || team.hasPermission("ab.tempmute")) {
											team.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("mute_success.all").replace("%player%", args[0]).replace("%from%", senderName).replace("%duration%", args[1] + " " + args[2]));
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
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("bm_value_to_high"));
					}
				}catch(Exception ex) {
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("bm_value_must_be_number"));
				}
			}else {
				AdvancedBanMain.sendBanHelp(sender, 4);
			}
		}else {
			sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("no_perm"));
		}
	}
}
