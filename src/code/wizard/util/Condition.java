package code.wizard.util;

import org.bukkit.ChatColor;

public enum Condition {

	POISON(ChatColor.DARK_GREEN+"Poisoned", ChatColor.DARK_GREEN+"Poison "+ChatColor.DARK_AQUA+"deals 10 damage every 2 seconds"), 
	BURN(ChatColor.RED+"Burned", ChatColor.RED+"Burn "+ChatColor.DARK_AQUA+"deals 10 damage every 2 seconds"),
	WEAK(ChatColor.GRAY+"Weakend", ChatColor.GRAY+"Weakend "+ChatColor.DARK_AQUA+"players take double damage"),
	SLOW(ChatColor.DARK_PURPLE+"Slowed", ChatColor.DARK_PURPLE+"Slowed "+ChatColor.DARK_AQUA+"players walk half as fast"),
	OIL(ChatColor.DARK_RED+"Oiled", ChatColor.DARK_RED+"Oiled "+ChatColor.DARK_AQUA+"players can't heal"),
	TIRED(ChatColor.GRAY+"Tired", ChatColor.GRAY+"Tires "+ChatColor.DARK_AQUA+"players deal half damage"),
	SUPER(ChatColor.RED+"S"+ChatColor.YELLOW+"u"+ChatColor.GREEN+"p"+ChatColor.AQUA+"e"+ChatColor.LIGHT_PURPLE+"r", 
			ChatColor.RED+"S"+ChatColor.YELLOW+"u"+ChatColor.GREEN+"p"+ChatColor.AQUA+"e"+ChatColor.LIGHT_PURPLE+"r"+ChatColor.DARK_AQUA+" players deal double damage!"),
	ENDGAME(ChatColor.BLACK+"Endgame", ChatColor.BLACK+"Endgame"+ChatColor.DARK_AQUA+" doubles mana regeneration, hurts 5 damage every 2 seconds and doubles damage"); //part of Endgame code
	
	String name;
	String reminder;
	
	
	private Condition(String n, String r){
		name = n;
		reminder = r;
	}
	
	public String getName(){
		return name;
	}
	
	public String getReminder(){
		return reminder;
	}
}
