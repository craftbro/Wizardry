package code.wizard.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.dsh105.holoapi.HoloAPI;

import code.wizard.armor.Armor;
import code.wizard.main.Main;
import code.wizard.spell.Spell;
import code.wizard.spell.SpellLoader;

public class FindManager {
	
	Main plugin;
	
	public FindManager(Main instance){
		plugin = instance;
	}
	
	public void findItem(Player p){
		
		Random r = new Random();
		
		int change = r.nextInt(100)+1;
		
	
		
		if(change <= 50){			
			findSpell(p);
		}else{
	
			int change2 = r.nextInt(100)+1;
			
			
			
			if(change2 <= 25){
				findHat(p);
			}else if(change2 > 25 && change2 <= 50){
				findCape(p);
			}else if(change2 > 50 && change2 <= 75){
				findPants(p);
			}else if(change2 > 75){
				findBoots(p);
			}
			
		}
		
	}
	
	public void findSpell(Player p){
		
		Spell spell = null;
		List<Spell> sp = new ArrayList<Spell>();
		
		for(Spell s : SpellLoader.spells.values()){
			if(s.isFindable() && !plugin.sql.handler.hasSpell(p, s))  sp.add(s);
		}
		
		if(sp.size() == 0) return;
		spell = sp.get(new Random().nextInt(sp.size()));	
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You found '"+spell.getName()+ChatColor.LIGHT_PURPLE+"' spell!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");		
		plugin.sql.handler.giveSpell(p, spell);	
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}
	
	
	public void findHat(Player p){
		
		Armor.hat spell = null;
		List<Armor.hat> sp = new ArrayList<Armor.hat>();
		
		for(Armor.hat s : Armor.hat.values()){
			if(s.isFindable() && !plugin.sql.handler.hasHat(p, s))  sp.add(s);
		}
		
		if(sp.size() == 0) return;
		spell = sp.get(new Random().nextInt(sp.size()));	
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You found '"+spell.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' hat!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");		
		plugin.sql.handler.giveHat(p, spell);	
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}
	

	public void findCape(Player p){
		
		Armor.cape spell = null;
		List<Armor.cape> sp = new ArrayList<Armor.cape>();
		
		for(Armor.cape s : Armor.cape.values()){
			if(s.isFindable() && !plugin.sql.handler.hasCape(p, s))  sp.add(s);
		}
		
		if(sp.size() == 0) return;
		spell = sp.get(new Random().nextInt(sp.size()));	
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You found '"+spell.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' cape!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");		
		plugin.sql.handler.giveCape(p, spell);	
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}
	

	public void findPants(Player p){
		
		Armor.pants spell = null;
		List<Armor.pants> sp = new ArrayList<Armor.pants>();
		
		for(Armor.pants s : Armor.pants.values()){
			if(s.isFindable() && !plugin.sql.handler.hasPants(p, s))  sp.add(s);
		}
		
		if(sp.size() == 0) return;
		spell = sp.get(new Random().nextInt(sp.size()));	
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You found '"+spell.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' pants!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");		
		plugin.sql.handler.givePants(p, spell);	
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}
	

	public void findBoots(Player p){
		
		Armor.boots spell = null;
		List<Armor.boots> sp = new ArrayList<Armor.boots>();
		
		for(Armor.boots s : Armor.boots.values()){
			if(s.isFindable() && !plugin.sql.handler.hasBoots(p, s))  sp.add(s);
		}
		
		if(sp.size() == 0) return;
		spell = sp.get(new Random().nextInt(sp.size()));	
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You found '"+spell.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' boots!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");		
		plugin.sql.handler.giveBoots(p, spell);	
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}
	

	
public void giveSpell(Player p, String s, Spell spell){
		
		if(plugin.sql.handler.hasSpell(p, spell)) return;
		

		
		p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You got '"+spell.getName()+ChatColor.LIGHT_PURPLE+"' for "+ChatColor.GREEN+s+ChatColor.LIGHT_PURPLE+"!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");
		
		plugin.sql.handler.giveSpell(p, spell);
		
		p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
	}

public void giveHat(Player p, String s, Armor.hat hat){
	if(plugin.sql.handler.hasHat(p, hat)) return;
	p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You got '"+hat.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' for "+ChatColor.GREEN+s+ChatColor.LIGHT_PURPLE+"!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");	
	plugin.sql.handler.giveHat(p, hat);	
	p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
}

public void giveCape(Player p, String s, Armor.cape hat){
	if(plugin.sql.handler.hasCape(p, hat)) return;
	p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You got '"+hat.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' for "+ChatColor.GREEN+s+ChatColor.LIGHT_PURPLE+"!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");	
	plugin.sql.handler.giveCape(p, hat);	
	p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
}

public void givePants(Player p, String s, Armor.pants hat){
	if(plugin.sql.handler.hasPants(p, hat)) return;
	plugin.sql.handler.givePants(p, hat);	
	p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You got '"+hat.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' for "+ChatColor.GREEN+s+ChatColor.LIGHT_PURPLE+"!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");	
	p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
}
public void giveBoots(Player p, String s, Armor.boots hat){
	if(plugin.sql.handler.hasBoots(p, hat)) return;
	p.sendMessage(ChatColor.GREEN+""+ChatColor.MAGIC+"H"+plugin.getPersonalPrefix()+ChatColor.LIGHT_PURPLE+"You got '"+hat.getStack().getItemMeta().getDisplayName()+ChatColor.LIGHT_PURPLE+"' for "+ChatColor.GREEN+s+ChatColor.LIGHT_PURPLE+"!"+ChatColor.GREEN+""+ChatColor.MAGIC+"H");	
	plugin.sql.handler.giveBoots(p, hat);	
	p.playSound(p.getEyeLocation(), Sound.LEVEL_UP, 100, 2);
}


}
