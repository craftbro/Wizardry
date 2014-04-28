package code.wizard.maps;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Map {
	
	public static final Map battlefield = new Map(ChatColor.DARK_GREEN+"Battlefield", new Location(Bukkit.getWorld("world"), 232, 78, -919));
	public static final Map bridge = new Map(ChatColor.AQUA+"Bridge", new Location(Bukkit.getWorld("world"), 232, 78, -919));
	
	Location spawn;
	String name;
	
	public Map(String s, Location spawn){
		name = s;
		this.spawn = spawn;
	}
	
	public Location getSpawn(){
		return spawn;
	}
	
	public String getName(){
		return name;
	}

}
