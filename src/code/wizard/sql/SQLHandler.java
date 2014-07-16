package code.wizard.sql;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import code.wizard.armor.Armor;
import code.wizard.spell.Spell;

public class SQLHandler {
	
	SQLBase sql;
	
	public SQLHandler(SQLBase base){
		sql = base;
	}
	
	public int getScore(Player p){
		return (int) sql.getData(p, "rank");
	}
	
	public void addScore(Player p, int add){
		sql.alterData(p, "rank", getScore(p)+add);
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
	
public void giveHat(Player p, Armor.hat hat){	
		String back = "";		
		String spellsRaw = (String) sql.getData(p, "armor");
		String[] spellsArray = new String[1];		
		if(!spellsRaw.contentEquals("none")){
			spellsArray = spellsRaw.split(",");	
		List<String> spells = Arrays.asList(spellsArray);		
		for(String sp : spells){
			back+=sp;
			back+=",";
		}		
		}
		back+=ChatColor.stripColor(hat.name());
		sql.alterData(p, "armor", back);		
	}

public void giveCape(Player p, Armor.cape hat){	
	String back = "";		
	String spellsRaw = (String) sql.getData(p, "armor");
	String[] spellsArray = new String[1];		
	if(!spellsRaw.contentEquals("none")){
		spellsArray = spellsRaw.split(",");	
	List<String> spells = Arrays.asList(spellsArray);		
	for(String sp : spells){
		back+=sp;
		back+=",";
	}		
	}
	back+=ChatColor.stripColor(hat.name());
	sql.alterData(p, "armor", back);		
}

public void givePants(Player p, Armor.pants hat){	
	String back = "";		
	String spellsRaw = (String) sql.getData(p, "armor");
	String[] spellsArray = new String[1];		
	if(!spellsRaw.contentEquals("none")){
		spellsArray = spellsRaw.split(",");	
	List<String> spells = Arrays.asList(spellsArray);		
	for(String sp : spells){
		back+=sp;
		back+=",";
	}		
	}
	back+=ChatColor.stripColor(hat.name());
	sql.alterData(p, "armor", back);		
}

public void giveBoots(Player p, Armor.boots hat){	
	String back = "";		
	String spellsRaw = (String) sql.getData(p, "armor");
	String[] spellsArray = new String[1];		
	if(!spellsRaw.contentEquals("none")){
		spellsArray = spellsRaw.split(",");	
	List<String> spells = Arrays.asList(spellsArray);		
	for(String sp : spells){
		back+=sp;
		back+=",";
	}		
	}
	back+=ChatColor.stripColor(hat.name());
	sql.alterData(p, "armor", back);		
}
	
	public boolean hasSpell(Player p, Spell spell){
		String spellsRaw = sql.getSpells(p);
		String[] spellsArray = new String[1];
		
		if(!spellsRaw.contentEquals("none")) spellsArray = spellsRaw.split(",");
		
		List<String> spells = Arrays.asList(spellsArray);
		
		return spells.contains(ChatColor.stripColor(spell.getName()));
	
	}
	
	public boolean hasHat(Player p, Armor.hat hat){
		String spellsRaw = (String) sql.getData(p, "armor");
		String[] spellsArray = new String[1];		
		if(!spellsRaw.contentEquals("none")) spellsArray = spellsRaw.split(",");		
		List<String> spells = Arrays.asList(spellsArray);		
		return spells.contains(hat.name());
	}
	
	public boolean hasCape(Player p, Armor.cape hat){
		String spellsRaw = (String) sql.getData(p, "armor");
		String[] spellsArray = new String[1];		
		if(!spellsRaw.contentEquals("none")) spellsArray = spellsRaw.split(",");		
		List<String> spells = Arrays.asList(spellsArray);		
		return spells.contains(hat.name());
	}
	
	public boolean hasPants(Player p, Armor.pants hat){
		String spellsRaw = (String) sql.getData(p, "armor");
		String[] spellsArray = new String[1];		
		if(!spellsRaw.contentEquals("none")) spellsArray = spellsRaw.split(",");		
		List<String> spells = Arrays.asList(spellsArray);		
		return spells.contains(hat.name());
	}
	
	public boolean hasBoots(Player p, Armor.boots hat){
		String spellsRaw = (String) sql.getData(p, "armor");
		String[] spellsArray = new String[1];		
		if(!spellsRaw.contentEquals("none")) spellsArray = spellsRaw.split(",");		
		List<String> spells = Arrays.asList(spellsArray);		
		return spells.contains(hat.name());
	}
	
	

}
