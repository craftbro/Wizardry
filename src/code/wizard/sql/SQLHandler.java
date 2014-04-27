package code.wizard.sql;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import code.wizard.spell.Spell;

public class SQLHandler {
	
	SQLBase sql;
	
	public SQLHandler(SQLBase base){
		sql = base;
	}
	
	public void giveSpell(Player p, Spell spell){
		
		String back = "";
		
		String spellsRaw = sql.getSpells(p);
		String[] spellsArray = new String[1];
		
		if(!spellsRaw.contentEquals("none")){
			spellsArray = spellsRaw.split(",");
		
		
		List<String> spells = Arrays.asList(spellsArray);
		
	
		
		
				
		for(String sp : spells){
			back+=sp;
			back+=",";
		}
		
		}
		
		back+=ChatColor.stripColor(spell.getName());

		sql.alterData(p, "spells", back);
		
	}
	
	public boolean hasSpell(Player p, Spell spell){
		String spellsRaw = sql.getSpells(p);
		String[] spellsArray = new String[1];
		
		if(!spellsRaw.contentEquals("none")) spellsArray = spellsRaw.split(",");
		
		List<String> spells = Arrays.asList(spellsArray);
		
		return spells.contains(ChatColor.stripColor(spell.getName()));
	
	}
	
	

}
