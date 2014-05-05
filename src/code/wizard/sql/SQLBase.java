package code.wizard.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import code.wizard.main.Main;

public class SQLBase {

	static Main plugin;
	
	private static Connection connection;
	public SQLHandler handler;
	
	public SQLBase(Main instance){
		plugin = instance;
		handler = new SQLHandler(this);
	}
	
	public synchronized static void closeConnection(){
		try{
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public synchronized static void openConnection(){
		try{
			connection = DriverManager
					.getConnection("jdbc:mysql://db6.hosting2go.nl:42944/wdt", 
							"m1_87645bc2", 
							"adB24UFMLY");			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public synchronized static void query(Player p,String q, String c){
		
		openConnection();
		
		plugin.print("doing query "+q+", with argument "+c);
	
		try {
			PreparedStatement sql = 
					connection.prepareStatement(q);
			
		
				
		if(c.contentEquals("")){
			sql.executeUpdate();
			p.sendMessage("updated!");
		}else{
		ResultSet result = sql.executeQuery();
		result.next();
		
		Object b = result.getObject(c);
		
	p.sendMessage("Querry Returned: "+b);
		}
			
			sql.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
	
		}finally{
			closeConnection();
		}
		
	}
	
	public synchronized static boolean isPlayerInDatabase(Player p){
		try {
			PreparedStatement sql = 
					connection.prepareStatement("SELECT * FROM `wdt` WHERE username=?;");
			sql.setString(1, p.getName());
			ResultSet result = sql.executeQuery();
			
			boolean isThere = result.next();
			
			sql.close();
			result.close();
			
			return isThere;
			
		} catch (SQLException e) {
			e.printStackTrace();
			
			return false;
		}		
	}
	
	public synchronized Object getData(Player p, String collum){
		
		openConnection();
				
		
				
				try {
					PreparedStatement sql = 
							connection.prepareStatement("SELECT "+collum+" FROM `wdt` WHERE id=?;");
					
							sql.setString(1, p.getUniqueId().toString());
					ResultSet result = sql.executeQuery();
					
					result.next();
					
					Object output = result.getObject(collum);
					
					
					result.close();
					sql.close();
					
					return output;
					
				} catch (SQLException e) {
					e.printStackTrace();
					return 0;
				}finally{
					closeConnection();
				}
				
				
			}
	
	public synchronized String getSpells(Player p){
		
openConnection();
		
		if(!isPlayerInDatabase(p)) return null;
		
		try {
			PreparedStatement sql = 
					connection.prepareStatement("SELECT spells FROM wdt WHERE id=?;");
			
			sql.setString(1,p.getUniqueId().toString());
			ResultSet result = sql.executeQuery();
			
			result.next();
			
			String spells = result.getString("spells");
			
			
			result.close();
			sql.close();
			
			return spells;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			closeConnection();
		}
		
		
	}
	
	public synchronized static void alterData(Player p, String change, Object nd){
		
		   openConnection();
				
			
				try {
					PreparedStatement sql = 
							connection.prepareStatement("UPDATE `wdt` SET "+change+"=? WHERE id=?;");
					
		
					sql.setObject(1, nd);
					sql.setString(2, p.getUniqueId().toString());
					
					sql.executeUpdate();
					sql.close();
					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					closeConnection();
				}
				
			}
	
	
	
	public synchronized static void addPlayer(Player p){
		openConnection();
		
		if(isPlayerInDatabase(p)) return;
		
		try {
			PreparedStatement sql = 
					connection.prepareStatement("INSERT INTO `wdt` VALUES(?, ?, ?, ?, 0, 0, 0);");
			
			sql.setString(1, p.getUniqueId().toString());
			sql.setString(2, p.getName());
			sql.setString(3, "none");
			sql.setString(4, "none");
			
			sql.execute();
			sql.close();
			
			plugin.print("Added "+p.getName()+" to the database");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeConnection();
		}
	}
	
	
	
	
	
	
	
	
}
