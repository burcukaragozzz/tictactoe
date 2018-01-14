package com.burcu.tictactoe;

import java.sql.*;

public class Driver {
	
	private Connection con;
	private String url = "jdbc:mysql://localhost/tictactoe?autoReconnect=true&useSSL=false";
	private String username = "root";
	private String password = "qweasdewq";
	
	public Driver() {
		try {
			con = DriverManager.getConnection(url,username,password);
		} 
		catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public int firstOrCreatePlayer(String name) throws SQLException 
	{
		PreparedStatement stm = con.prepareStatement("SELECT * FROM Players WHERE name = ?");
		stm.setString(1, name);
		ResultSet result = stm.executeQuery();
		
		if(!result.next()) {
			stm = con.prepareStatement("INSERT INTO Players (name) VALUES (?)");
			stm.setString(1, name);
			stm.executeUpdate();
			return -1;
		}
		
		return result.getInt("total_wins");
	}
	
	public int playerWon(String name) throws SQLException 
	{	
		PreparedStatement stm = con.prepareStatement("SELECT * FROM Players WHERE name = ?");
		stm.setString(1, name);
		ResultSet result = stm.executeQuery();
		int total_wins = -1;
		
		if(result.next()) {
			total_wins = result.getInt("total_wins");
		}
		
		stm = con.prepareStatement("UPDATE Players SET total_wins = ? WHERE name = ?");
		total_wins += 1;
		stm.setInt(1, total_wins);
		stm.setString(2, name);
		stm.executeUpdate();
		
		return total_wins;
	}
	
	public int newGame(String name) throws SQLException 
	{
		PreparedStatement stm = con.prepareStatement("INSERT INTO Games (player_1, player_2) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
		stm.setString(1, name);
		stm.setString(2,name);
		stm.executeUpdate();
		ResultSet result = stm.getGeneratedKeys();
		if(result.next()) {
			return result.getInt(1); 
		}
		
		return -1;
	}
	
	public void updateGameInfo(int id, String player_1, String player_2) throws SQLException
	{
		PreparedStatement stm = con.prepareStatement("UPDATE Games SET player_1 = ?, player_2 = ? WHERE id = ?");
		stm.setString(1, player_1);
		stm.setString(2, player_2);
		stm.setInt(3, id);
		stm.executeUpdate();
	}
	
	
	
}