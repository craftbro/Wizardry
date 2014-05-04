package code.wizard.maps;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import code.wizard.lobby.Lobby1v1;
import code.wizard.main.Main;
import code.wizard.player.KitManager;
import code.wizard.player.WizTeam;

public class Arena {

	public static List<Arena> LIST = Arrays.asList(new Arena[]{
			new Arena("arena 1", new Location(Bukkit.getWorld("world"), 402.96, 58, -1006.12)),
			new Arena("arena 2", new Location(Bukkit.getWorld("world"), 402.96, 58, -943.93))
	});

	
	Location spawn;
	String name = "Undefined Arena [ERROR]";
	
	public boolean occupied = false; 
	
	Player p1 = null;
	Player p2 = null; 
	
	public int peace = 15;
	public boolean pp = true;
	
	
	public Arena(String s, Location spawn){
		name = s;
		this.spawn = spawn;
	}
	
	public Location getSpawn(){
		return spawn;
	}
	
	public String getName(){
		return name;
	}
	
	public void tick(){
		if(pp){
			if(peace <= 0){ 
				pp = false;
			
			p1.sendMessage(Main.getPrefix()+"The battle has begun!");
			p2.sendMessage(Main.getPrefix()+"The battle has begun!");
			}else{		
			peace--;
			}
		}
	}
	
	public void kill(Player p){
		if(p2 == p){
			p2.sendMessage(Main.getPersonalPrefix()+"You"+ChatColor.RED+" lost"+ChatColor.GOLD+"! Better luck next time!");
			p1.sendMessage(Main.getPersonalPrefix()+"You"+ChatColor.GREEN+" won"+ChatColor.GOLD+"!");
			Main.getInstance().lobby.giveWin(p1);
			Main.getInstance().lobby.giveLose(p2);
			
			p1.hidePlayer(p2);
			
			KitManager.unregister(p1);
			KitManager.unregister(p2);
			
			p2.getInventory().clear();
			
			p2.teleport(spawn);
			
		}else if(p1 == p){
			p1.sendMessage(Main.getPersonalPrefix()+"You"+ChatColor.RED+" lost"+ChatColor.GOLD+"! Better luck next time!");
			p2.sendMessage(Main.getPersonalPrefix()+"You"+ChatColor.GREEN+" won"+ChatColor.GOLD+"!");
			Main.getInstance().lobby.giveWin(p2);
			Main.getInstance().lobby.giveLose(p1);
			
			p2.hidePlayer(p1);
			
			KitManager.unregister(p1);
			KitManager.unregister(p2);		
			
			p1.getInventory().clear();
			
			p1.teleport(spawn);
	

		}else{
			return;
		}
		
		
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable(){

			@Override
			public void run() {
				Lobby1v1 lobby = (Lobby1v1) Main.getInstance().lobby;
				
				lobby.handleRespawn(p1);
				lobby.handleRespawn(p2);
				
				lobby.removeFromList(p1);
				lobby.removeFromList(p2);

				occupied = false;
				peace = 15;
				pp = true;

				
				p1 = null;
				p2 = null;
				
			}
			
		}, 100);
	}
	
	public void start(Player p1, Player p2){
		
		
		p1.teleport(spawn);
		p2.teleport(spawn);
		
		Main.getInstance().km.giveKit(p1, new WizTeam(p1.getName()));
		Main.getInstance().km.giveKit(p2, new WizTeam(p2.getName()));
		
		this.p1 = p1;
		this.p2 = p2;
		
		occupied = true;
	}
	
	
}
