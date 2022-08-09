package de.ab.delta203.bungee.mysql.modules;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.ab.delta203.bungee.AdvancedBanMain;

public class GetMySQl_Report {

	public static void report(String uuid, String fuuid, String reason, String server) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime time = LocalDateTime.now();
		String sTime = dtf.format(time);
		long millis = System.currentTimeMillis();
		
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("INSERT INTO AB_Reports (PlayerUUID,FromUUID,DateAndTime,CurrentMillis,Server,Reason) VALUES (?,?,?,?,?,?)");
			ps.setString(1, uuid);
			ps.setString(2, fuuid);
			ps.setString(3, sTime);
			ps.setLong(4, millis);
			ps.setString(5, server);
			ps.setString(6, reason);
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static boolean hasAlreadyReportedTarget(String uuid, String tuuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT PlayerUUID FROM AB_Reports WHERE FromUUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(tuuid.equals(rs.getString("PlayerUUID"))) return true;
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static int getReportsCount() {
		int result = 0;
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT PlayerUUID FROM AB_Reports");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				result++;
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
