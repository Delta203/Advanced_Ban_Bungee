package de.ab.delta203.bungee.commands;

import java.util.HashMap;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.ConfigYML;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerInfo;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_Report;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_Report extends Command {

	private HashMap<CommandSender, String> confirm = new HashMap<>();
	private HashMap<CommandSender, String> confirm_ = new HashMap<>();
	
	public Command_Report() {
		super("report");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("templates")) {
				sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_templates.title"));
				for(String str : MessagesYML.get().getStringList("report_templates.list")) {
					sender.sendMessage(MessagesYML.get().getString("report_templates.prefix") + str);
				}
			}else if(args[0].equalsIgnoreCase("$confirm")) {
				if(confirm.containsKey(sender)) {
					String target = confirm.get(sender);
					String targetUUID = GetMySQl_PlayerInfo.getUUIDByName(target);
					String senderUUID = "Console";
					String reason = confirm_.get(sender);
					String server = "Console";
					if(sender instanceof ProxiedPlayer) {
						senderUUID = ((ProxiedPlayer) sender).getUniqueId().toString();
						server = ((ProxiedPlayer) sender).getServer().getInfo().getName();
					}
					
					if(!GetMySQl_Report.hasAlreadyReportedTarget(senderUUID, targetUUID)) {
						GetMySQl_Report.report(targetUUID, senderUUID, reason, server);
						
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_confirmed").replace("%player%", target));
						if(ConfigYML.get().getBoolean("notify_report")) {
							for(ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
								if(team.hasPermission("ab.panel")) {
									team.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_notify_others").replace("%reportscount%", String.valueOf(GetMySQl_Report.getReportsCount())));
								}
							}
						}
					}else {
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_target_already_reported").replace("%player%", target));
					}
					
					confirm.remove(sender);
					confirm_.remove(sender);
				}else {
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_cant_be_confirmed"));
				}
			}else {
				ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
				if(target != null) {
					if(!target.equals(sender)) {
						if(!target.hasPermission("ab.cantbereported")) {
							sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_player.title").replace("%player%", target.getName()));
							sender.sendMessage(MessagesYML.get().getString("report_player.reason"));
							
							ComponentBuilder reasons = new ComponentBuilder();
							for(String str : MessagesYML.get().getStringList("report_templates.list")) {
								TextComponent tc = new TextComponent(MessagesYML.get().getString("report_player.format").replace("%reason%", str));
								tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report " + target.getName() + " " + str.replace("§", "&")));
								tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessagesYML.get().getString("report_player.hover").replace("%player%", target.getName())).create()));
								tc.addExtra(" ");
								reasons.append(tc);
							}
							sender.sendMessage(reasons.create());
						}else {
							sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_target_cant_be_reported").replace("%player%", target.getName()));
						}
					}else {
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("you_cant_be_target"));
					}
				}else {
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("player_not_online").replace("%player%", args[0]));
				}
			}
		}else if(args.length == 2) {
			ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);
			String reason = args[1].replace("&", "§");
			if(target != null) {
				if(!target.equals(sender)) {
					if(!target.hasPermission("ab.cantbereported")) {
						if(MessagesYML.get().getStringList("report_templates.list").contains(reason)) {
							sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_confirm.title").replace("%player%", target.getName()));
							sender.sendMessage(MessagesYML.get().getString("report_confirm.info").replace("%player%", target.getName()).replace("%reason%", reason));
							
							TextComponent tc = new TextComponent(MessagesYML.get().getString("report_confirm.confirm"));
							tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/report $confirm"));
							tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessagesYML.get().getString("report_confirm.hover").replace("%player%", target.getName())).create()));
							sender.sendMessage(tc);
							confirm.put(sender, target.getName());
							confirm_.put(sender, reason);
						}else {
							sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_reason_doesnt_exist"));
						}
					}else {
						sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("report_target_cant_be_reported").replace("%player%", target.getName()));
					}
				}else {
					sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("you_cant_be_target"));
				}
			}else {
				sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("player_not_online").replace("%player%", args[0]));
			}
		}else {
			AdvancedBanMain.sendReportHelp(sender);
		}
	}	
}
