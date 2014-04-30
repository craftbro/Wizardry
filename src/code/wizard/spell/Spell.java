package code.wizard.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import code.wizard.item.NamedStack;
import code.wizard.main.Main;

public class Spell implements Comparable<Spell>, Cloneable{

	//The player that owns the spell (Might be null!)*/
	protected Player p;
	//The spell name, as shown in the inventory and saved in SQL database
	protected String name = "Basic Spell";
	//The stack shown in the player's inventory
	protected ItemStack stack = new ItemStack(Material.WOOD_BUTTON);
	
	//The spell description
	protected List<String> des = new ArrayList<String>();
	//All reminders (if your spell gives poison, do 'rem.add(Condition.POISON.getReminder())
	protected List<String> rem = new ArrayList<String>();
	
	//The infos (Like, for Range: 5, do info.put("Range", "5"))
	protected HashMap<String, String> info = new HashMap<String, String>();
	
	//The mana cost (max 100)
	protected float cost = 0;
	
	//The slot teh spell needs to be in
	protected SpellSlot slot = SpellSlot.PRIMARY_MELEE;
	
	//Says if the spell is owned from beginning (false = free from begin)
	protected boolean unlockable = false;

	//Says if the spell is awarded by an achievement (true = not achievable)
	protected boolean findable = false;
	
	protected int cooldown = 0;
	
	public Spell(Player p){
		this.p = p;
	}
	
	/**
     * Changes spell's owner
     * @param p The new player 
     */
	public void setPlayer(Player p){
		this.p = p;
	}
	
	/**
     * Returns the spells slot
     */
	public SpellSlot getSlot(){
		return slot;
	}
	
	/**
     * Returns the spells name
     */
	public String getName(){
		return name;
	}
	
	/**
     * Returns if the spell is unlockable
     */
	public boolean isUnlockable(){
		return unlockable;
	}
	
	/**
     * Returns if the spell is findable
     */
	public boolean isFindable(){
		return findable;
	}
	
	/**
     * Returns a copy of the spell
     */
	protected Object clone()
	  {
	    try
	    {
	      return super.clone();
	    }
	    catch (CloneNotSupportedException ex) {
	    }
	    return null;
	  }
	
	/**
     * Returns the spell's owner (migth be null!)
     */
	public Player getPlayer(){
		return p;
	}
	
	/**
     * Fired when a player attempts to cast the spell
     */
	public void use(){
		float xp = p.getExp();
		float xpCost = cost/100;
		
		if(xp >= xpCost){
			if(cooldown > 0){ p.sendMessage(Main.getPersonalPrefix()+"This spell is still cooling down");return;}
			p.setExp(xp - xpCost);
			
			cast();
		}
	}
	
	/**
     * Fired when the player succesfuly casted the spell, the mana is already removed
     */
	public void cast(){}
	
	
	/**
     * Formats the spells lore
     */
	private String[] format(){
		List<String> ds = new ArrayList<String>();
		
		ds.add(ChatColor.AQUA+""+ChatColor.UNDERLINE+"Mana Cost:"+ChatColor.RESET+" "+ChatColor.WHITE+ChatColor.BOLD+""+(int)cost);
		ds.add(" ");
		ds.addAll(des);
		ds.add(" ");
		for(String i : info.keySet()){
			String i2 = info.get(i);
			
			ds.add(ChatColor.WHITE+i+ChatColor.WHITE+": "+i2);
		}
		
		if(rem.size() > 0){
			ds.add(" ");
			ds.addAll(rem);
		}
		
		String[] lore = new String[ds.size()];
		
		int t = 0;
		
		for(String s : ds){
			lore[t] = s;
			t++;
		}
		
		return lore;
	}
	
	/**
     * Returns the spells itemstack (including lore)
     */
	public ItemStack getStack(){
	if(cooldown == 0){
		return new NamedStack(name, stack.getType(), 1, stack.getData().getData(),  format());
	}else{
		return new NamedStack(name, stack.getType(), cooldown, stack.getData().getData(),  format());
	}
	}
	
	public void tick(){
		if(cooldown > 0) cooldown--;
	}
	
	
    @Override
    public int compareTo(Spell other) {

	return 0;
    }

}
