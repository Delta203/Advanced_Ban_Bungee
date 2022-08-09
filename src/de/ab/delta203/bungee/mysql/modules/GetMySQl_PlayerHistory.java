package de.ab.delta203.bungee.mysql.modules;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.ab.delta203.bungee.AdvancedBanMain;

public class GetMySQl_PlayerHistory {

	public static void insert(String uuid, String fuuid, String type, String reason, String end) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime time = LocalDateTime.now();
		String sTime = dtf.format(time);
		
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("INSERT INTO AB_PlayerHistory (PlayerUUID,PlayerName,FromUUID,DateAndTime,Type,Reason,End) VALUES (?,?,?,?,?,?,?)");
			ps.setString(1, uuid);
			ps.setString(2, GetMySQl_PlayerInfo.getNameByUUID(uuid));
			ps.setString(3, fuuid);
			ps.setString(4, sTime);
			ps.setString(5, type);
			ps.setString(6, reason);
			ps.setString(7, end);
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}
