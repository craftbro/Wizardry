package code.wizard.maps;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Map {
	
	public static final Map battlefield = new Map(ChatColor.DARK_GREEN+"Battlefield", null);
	
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
