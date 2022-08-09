package de.ab.delta203.bungee.mysql.modules;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.MessagesYML;

public class GetMySQl_Ban {

	public static void ban(String uuid, String ip, String fuuid, String reason, long end) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("INSERT INTO AB_Bans (PlayerUUID,PlayerIP,FromUUID,Reason,End) VALUES (?,?,?,?,?)");
			ps.setString(1, uuid);
			ps.setString(2, ip);
			ps.setString(3, fuuid);
			ps.setString(4, reason);
			ps.setLong(5, end);
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void unban(String uuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("DELETE FROM AB_Bans WHERE PlayerUUID = ?");
			ps.setString(1, uuid);
			ps.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static boolean isBannedUUID(String uuid) {
		try {
			PreparedStatement psNormal = AdvancedBanMain.mysql.con.prepareStatement("SELECT FromUUID FROM AB_Bans WHERE PlayerUUID = ?");
			psNormal.setString(1, uuid);
			ResultSet rsNormal = psNormal.executeQuery();
			while(rsNormal.next()) {
				return true;
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static boolean isBannedIP(String ip) {
		try {
			PreparedStatement psIP = AdvancedBanMain.mysql.con.prepareStatement("SELECT PlayerUUID FROM AB_Bans WHERE PlayerIP = ?");
			psIP.setString(1, ip);
			ResultSet rsIP = psIP.executeQuery();
			while(rsIP.next()) {
				return true;
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public static String getReason(String uuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT Reason FROM AB_Bans WHERE PlayerUUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString("Reason");
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String getFrom(String uuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT FromUUID FROM AB_Bans WHERE PlayerUUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString("FromUUID");
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static long getEnd(String uuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT End FROM AB_Bans WHERE PlayerUUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getLong("End");
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return 0L;
	}
	
	public static String getEndString(String uuid) {
		long current = System.currentTimeMillis();
		long end = getEnd(uuid);
		if(end == -1) return MessagesYML.get().getString("BanUnit.permanent.name");
		long millis = end-current;
		
		long seconds = 0L;
		long minutes = 0L;
		long hours = 0L;
		long days = 0L;
		while(millis > 1000) {
			millis -= 1000;
			seconds++;
		}while(seconds > 60) {
			seconds -= 60;
			minutes++;
		}while(minutes > 60) {
			minutes -= 60;
			hours++;
		}while(hours > 24) {
			hours -= 24;
			days++;
		}
		
		return days + " " + MessagesYML.get().getString("BanUnit.days.name") + " "
				+ hours + " " + MessagesYML.get().getString("BanUnit.hours.name") + " "
				+ minutes + " " + MessagesYML.get().getString("BanUnit.minutes.name") + " "
				+ seconds + " " + MessagesYML.get().getString("BanUnit.seconds.name");
	}
}
