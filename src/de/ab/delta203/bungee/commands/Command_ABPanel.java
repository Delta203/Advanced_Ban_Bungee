package de.ab.delta203.bungee.commands;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.MessagesYML;
import de.ab.delta203.bungee.mysql.modules.GetMySQl_PlayerInfo;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Command_ABPanel extends Command {

	public Command_ABPanel() {
		super("abpanel");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			if(p.hasPermission("ab.panel")) {
				p.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("panel.title"));
				p.sendMessage(MessagesYML.get().getString("panel.info"));
				
				ComponentBuilder cb = new ComponentBuilder();
				TextComponent tc = new TextComponent(MessagesYML.get().getString("panel.link").replace("%loginkey%", GetMySQl_PlayerInfo.getLoginKey(p.getUniqueId().toString())));
				tc.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, MessagesYML.get().getString("panel.linkraw").replace("%loginkey%", GetMySQl_PlayerInfo.getLoginKey(p.getUniqueId().toString()))));
				cb.append(tc);
				
				p.sendMessage(cb.create());
			}else {
				p.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("no_perm"));
			}
		}else {
			sender.sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("you_must_be_a_player"));
		}
	}
}
