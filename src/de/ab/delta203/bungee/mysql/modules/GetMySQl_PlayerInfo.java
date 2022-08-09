package de.ab.delta203.bungee.mysql.modules;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.ab.delta203.bungee.AdvancedBanMain;

public class GetMySQl_PlayerInfo {
	
	public static void register(String uuid, String name) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("INSERT INTO AB_PlayerInfo (PlayerUUID,PlayerName,Server,LoginKey) VALUES (?,?,?,?)");
			ps.setString(1, uuid);
			ps.setString(2, name);
			ps.setString(3, "-");
			ps.setString(4, "-");
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static boolean isRegistered(String uuid) {
		return getNameByUUID(uuid) != null;
	}
	
	
	
	public static void updateName(String uuid, String name) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("UPDATE AB_PlayerInfo SET PlayerName = ? WHERE PlayerUUID = ?");
			ps.setString(1, name);
			ps.setString(2, uuid);
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void updateServer(String uuid, String server) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("UPDATE AB_PlayerInfo SET Server = ? WHERE PlayerUUID = ?");
			ps.setString(1, server);
			ps.setString(2, uuid);
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void updateLoginKey(String uuid, String key) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("UPDATE AB_PlayerInfo SET LoginKey = ? WHERE PlayerUUID = ?");
			ps.setString(1, key);
			ps.setString(2, uuid);
			ps.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static String getUUIDByName(String name) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT PlayerUUID FROM AB_PlayerInfo WHERE PlayerName = ?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString("PlayerUUID");
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String getNameByUUID(String uuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT PlayerName FROM AB_PlayerInfo WHERE PlayerUUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString("PlayerName");
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String getServer(String uuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT Server FROM AB_PlayerInfo WHERE PlayerUUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString("Server");
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String getLoginKey(String uuid) {
		try {
			PreparedStatement ps = AdvancedBanMain.mysql.con.prepareStatement("SELECT LoginKey FROM AB_PlayerInfo WHERE PlayerUUID = ?");
			ps.setString(1, uuid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return rs.getString("LoginKey");
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
