package code.wizard.player;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import code.configtesting.config.Config;
import code.wizard.main.Main;
import code.wizard.spell.Spell;

public class FindManager {
	
	Main plugin;
	
	public FindManager(Main instance){
		plugin = instance;
	}
	
	public void findItem(Player p){
		
	}
	
	public void findSpell(Player p,  Spell spell){
		
		if(plugin.sql.handler.hasSpell(p, spell)) return;
		
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You found '"+spell.getName()+ChatColor.LIGHT_PURPLE+"'!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");
		
		plugin.sql.handler.giveSpell(p, spell);
		
		
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}
	
public void giveSpell(Player p, String s, Spell spell){
		
		if(plugin.sql.handler.hasSpell(p, spell)) return;
		

		
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You got '"+spell.getName()+ChatColor.LIGHT_PURPLE+"' for "+ChatColor.GREEN+s+ChatColor.LIGHT_PURPLE+"!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");
		
		plugin.sql.handler.giveSpell(p, spell);
		
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}
	


}
