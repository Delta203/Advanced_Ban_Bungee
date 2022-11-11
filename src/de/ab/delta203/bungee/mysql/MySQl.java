package de.ab.delta203.bungee.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.ab.delta203.bungee.AdvancedBanMain;
import de.ab.delta203.bungee.files.MessagesYML;
import net.md_5.bungee.BungeeCord;

public class MySQl {

	public Connection con;
	private String url, database, user, password;
	private int port;
	
	
	public MySQl(String url, int port, String database, String user, String password) {
		this.url = url;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}
	
	@SuppressWarnings("deprecation")
	public void connect() {
		if(!isConnected()) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://" + this.url + ":" + port + "/" + this.database, this.user, this.password);
				BungeeCord.getInstance().getConsole().sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("connect_mysql"));
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void disconnect() {
		if(isConnected()) {
			try {
				con.close();
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private boolean isConnected() {
		return con != null;
	}
	
	@SuppressWarnings("deprecation")
	public void createTables() {
		if(isConnected()) {
			try {
				con.prepareStatement("CREATE TABLE IF NOT EXISTS AB_PlayerInfo (PlayerUUID VARCHAR(100),PlayerName VARCHAR(100),Server VARCHAR(100),LoginKey VARCHAR(100))").executeUpdate();
				con.prepareStatement("CREATE TABLE IF NOT EXISTS AB_PlayerHistory (PlayerUUID VARCHAR(100),PlayerName VARCHAR(100),FromUUID VARCHAR(100),DateAndTime VARCHAR(100),Type VARCHAR(100),Reason LONGTEXT,End VARCHAR(100))").executeUpdate();
				
				con.prepareStatement("CREATE TABLE IF NOT EXISTS AB_Bans (PlayerUUID VARCHAR(100),PlayerIP VARCHAR(100),FromUUID VARCHAR(100),Reason LONGTEXT,End VARCHAR(100))").executeUpdate();
				con.prepareStatement("CREATE TABLE IF NOT EXISTS AB_Mutes (PlayerUUID VARCHAR(100),FromUUID VARCHAR(100),Reason LONGTEXT,End VARCHAR(100))").executeUpdate();
				
				con.prepareStatement("CREATE TABLE IF NOT EXISTS AB_Reports (PlayerUUID VARCHAR(100),FromUUID VARCHAR(100),DateAndTime VARCHAR(100),CurrentMillis VARCHAR(100),Server VARCHAR(100),Reason LONGTEXT)").executeUpdate();
				con.prepareStatement("CREATE TABLE IF NOT EXISTS AB_Chat (PlayerUUID VARCHAR(100),PlayerName VARCHAR(100),DateAndTime VARCHAR(100),CurrentMillis VARCHAR(100),Server VARCHAR(100),Message LONGTEXT)").executeUpdate();
				
				con.prepareStatement("CREATE TABLE IF NOT EXISTS AB_CommandQuery (SenderUUID VARCHAR(100),Command VARCHAR(1000))").executeUpdate();
				BungeeCord.getInstance().getConsole().sendMessage(AdvancedBanMain.prefix + MessagesYML.get().getString("register_tables"));
			}catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
