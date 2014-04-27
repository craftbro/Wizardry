package code.wizard.util;

import org.bukkit.ChatColor;

public enum DamageType {

	FIRE(ChatColor.RED), 
	LIGHTNING(ChatColor.YELLOW), 
	WATER(ChatColor.AQUA), 
	POISON(ChatColor.DARK_GREEN), 
	AIR(ChatColor.WHITE), 
	GROUND(ChatColor.GREEN), 
	LIGHT(ChatColor.GOLD),
	DARK(ChatColor.GRAY),
	PHYSICAL(ChatColor.LIGHT_PURPLE);

	
	ChatColor color;
	
	
	private DamageType(ChatColor c){
		color = c;
	}
	
	public ChatColor getColor(){
		return color;
	}
	
}
