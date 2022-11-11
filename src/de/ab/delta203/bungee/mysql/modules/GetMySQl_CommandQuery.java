package de.ab.delta203.bungee.mysql.modules;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import de.ab.delta203.bungee.AdvancedBanMain;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class GetMySQl_CommandQuery {
	
	public static void fetchCommands() {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT * FROM AB_CommandQuery");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String senderuuid = rs.getString("SenderUUID");
				String command = rs.getString("Command");
				proceedCommand(senderuuid, command);
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("DELETE FROM AB_CommandQuery WHERE 1");
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void proceedCommand(String senderuuid, String command) {
		if(senderuuid.equalsIgnoreCase("Console")) {
			BungeeCord.getInstance().pluginManager.dispatchCommand(BungeeCord.getInstance().getConsole(), command);
			return;
		}
		
		UUID uuid = UUID.fromString("00000000-0000-0000-0000-000000000000");
		try {
			uuid = UUID.fromString(senderuuid);
		}catch(IllegalArgumentException ex) {}
		ProxiedPlayer p = BungeeCord.getInstance().getPlayer(uuid);
		if(p != null) BungeeCord.getInstance().pluginManager.dispatchCommand(p, command);
	}
}
