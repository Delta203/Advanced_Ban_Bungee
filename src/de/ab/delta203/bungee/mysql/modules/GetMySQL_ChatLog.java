package de.ab.delta203.bungee.mysql.modules;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.ab.delta203.bungee.AdvancedBanMain;

public class GetMySQL_ChatLog {

	public static void record(String uuid, String name, String server, String message) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime time = LocalDateTime.now();
		String sTime = dtf.format(time);
		
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("INSERT INTO AB_Chat (PlayerUUID,PlayerName,DateAndTime,CurrentMillis,Server,Message) VALUES (?,?,?,?,?,?)");
			ps.setString(1, uuid);
			ps.setString(2, name);
			ps.setString(3, sTime);
			ps.setLong(4, System.currentTimeMillis());
			ps.setString(5, server);
			ps.setString(6, message);
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
}
