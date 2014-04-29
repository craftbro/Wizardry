package code.wizard.armor;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import code.wizard.item.NamedStack;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;

public class Armor {

	final static String plus = ChatColor.RED+"+";
	final static String min = ChatColor.GREEN+"-";
final static String light = ChatColor.GOLD+" Light Damage";
	final static String dark = ChatColor.DARK_GRAY+" Dark Damage";
final static String fire = ChatColor.RED+" Fire Damage";
final static String ground = ChatColor.GREEN+" Ground Damage";
final static String water = ChatColor.AQUA+" Water Damage";
final static String air = ChatColor.WHITE+" Air Damage";
final static String lightning = ChatColor.YELLOW+" Lightning Damage";
	
	
	public enum hat{
		
	
		
		TIN_HAT(new NamedStack(ChatColor.GRAY+"Tin Hat", Material.IRON_HELMET, 
				new String[]{min+"25%"+light, plus+"25%"+dark}), 
				new DStat[]{new DStat(false, 25, DamageType.LIGHT), new DStat(true, 25, DamageType.DARK)}),
		KNIGHT_HAT(new NamedStack(ChatColor.GRAY+"Knight's Hat", Material.CHAINMAIL_HELMET, 
				new String[]{min+"20%"+light, min+"20%"+dark, plus+"40%"+ground,}),
				new DStat[]{new DStat(false, 20, DamageType.LIGHT), new DStat(false, 20, DamageType.DARK), new DStat(true, 40, DamageType.GROUND)}),
	    SHINY_HELMET(new NamedStack(ChatColor.AQUA+"Shiny Helmet", Material.DIAMOND_HELMET, 
				new String[]{min+"25%"+light, plus+"25%"+water,}),
				new DStat[]{new DStat(false, 25, DamageType.LIGHT),  new DStat(true, 25, DamageType.WATER)},
				true, true),
		PATIENCE_MASK(BasicUtil.setSkullOwner("MHF_Villager", new NamedStack(ChatColor.AQUA+"Patience Mask", Material.SKULL_ITEM, 1, (byte) 3, 
				new String[]{min+"15%"+dark, min+"15%"+air, plus+"10%"+water, plus+"10%"+light, plus+"10%"+fire})),
				new DStat[]{new DStat(false, 15, DamageType.DARK),  new DStat(false, 15, DamageType.AIR), new DStat(true, 10, DamageType.WATER), new DStat(true, 10, DamageType.LIGHT), new DStat(true, 10, DamageType.FIRE)},
				true);
		

	ItemStack s = null;
	List<DStat> stats = null;
	boolean unlockable = false;
	boolean findable = false;
	
	hat(ItemStack stack, DStat[] stats){
		s = stack;
	this.stats = Arrays.asList(stats);
	}
	
	hat(ItemStack stack, DStat[] stats , boolean un){
		s = stack;
	this.stats = Arrays.asList(stats);
	unlockable = un;
	}
	

	hat(ItemStack stack, DStat[] stats , boolean un, boolean fn){
		s = stack;
	this.stats = Arrays.asList(stats);
	unlockable = un;
	findable = fn;
	}
	
	public ItemStack getStack(){
		return s;
	}
	
	public boolean isUnlockable(){
		return unlockable;
	}
	
	public boolean isFindable(){
		return findable;
	}
	
	public List<DStat> getStats(){
		return stats;
	}
	
	public static hat getFromStack(ItemStack stack){
		for(hat h : hat.values()){
			if(stack.getItemMeta().getDisplayName().contentEquals(h.getStack().getItemMeta().getDisplayName())){
				return h;
			}
		}
		return null;
	}
	
	}
	
public enum cape{
		
	
		
		MAGIC_CAPE(new NamedStack(ChatColor.DARK_PURPLE+"Magic Cape", Material.LEATHER_CHESTPLATE, Color.PURPLE, 
				new String[]{min+"20%"+light, min+"20%"+dark, plus+"20%"+water, plus+"20%"+ground}), 
				new DStat[]{new DStat(false, 20, DamageType.LIGHT), new DStat(false, 20, DamageType.DARK), new DStat(true, 20, DamageType.WATER), new DStat(true, 20, DamageType.GROUND)}),
		ELEMENTAL_CAPE(new NamedStack(ChatColor.GREEN+"Elemental Cape", Material.LEATHER_CHESTPLATE, Color.GREEN, 
				new String[]{min+"25%"+fire, min+"25%"+ground, min+"25%"+water, min+"25%"+air,  plus+"50%"+light,  plus+"50%"+dark}),
				new DStat[]{new DStat(false, 25, DamageType.FIRE), new DStat(false, 25, DamageType.GROUND), new DStat(false, 25, DamageType.WATER), new DStat(false, 25, DamageType.AIR), new DStat(true, 50, DamageType.DARK), new DStat(true, 50, DamageType.LIGHT)}),
		BATCAPE(new NamedStack(ChatColor.DARK_GRAY+"Batcape", Material.LEATHER_CHESTPLATE, Color.BLACK, 
				new String[]{min+"20%"+fire, min+"20%"+dark,   plus+"40%"+light,}),
				new DStat[]{new DStat(false, 20, DamageType.FIRE), new DStat(false, 20, DamageType.DARK), new DStat(true, 40, DamageType.LIGHT)},
				true, true),
		SKILL_CAPE(new NamedStack(ChatColor.GOLD+"Skill Cape", Material.GOLD_CHESTPLATE, 
				new String[]{min+"25%"+light, min+"15%"+ground, plus+"10%"+air, plus+"30%"+dark}),
				new DStat[]{new DStat(false, 25, DamageType.LIGHT), new DStat(false, 15, DamageType.GROUND), new DStat(true, 10, DamageType.AIR), new DStat(true, 30, DamageType.DARK)},
				true);
		

	ItemStack s = null;
	List<DStat> stats = null;
	boolean unlockable = false;
	boolean findable = false;
	
	cape(ItemStack stack, DStat[] stats){
		s = stack;
	this.stats = Arrays.asList(stats);
	}
	
	cape(ItemStack stack, DStat[] stats , boolean un){
		s = stack;
	this.stats = Arrays.asList(stats);
	unlockable = un;
	}
	
	cape(ItemStack stack, DStat[] stats , boolean un, boolean fn){
		s = stack;
	this.stats = Arrays.asList(stats);
	unlockable = un;
	findable = fn;
	}
	
	public ItemStack getStack(){
		return s;
	}
	
	public boolean isUnlockable(){
		return unlockable;
	}
	
	public boolean isFindable(){
		return findable;
	}
	
	public List<DStat> getStats(){
		return stats;
	}
	
	public static cape getFromStack(ItemStack stack){
		for(cape h : cape.values()){
			if(stack.getItemMeta().getDisplayName().contentEquals(h.getStack().getItemMeta().getDisplayName())){
				return h;
			}
		}
		return null;
	}
	
	}

public enum pants{
	
	
	
	AQUA_TROUSERS(new NamedStack(ChatColor.AQUA+"Aqua Trousers", Material.LEATHER_LEGGINGS, Color.AQUA,
			new String[]{min+"30%"+water, plus+"30%"+lightning}), 
			new DStat[]{new DStat(false, 30, DamageType.WATER), new DStat(true, 30, DamageType.LIGHTNING)}),
	KINGS_LEGGINGS(new NamedStack(ChatColor.GRAY+"King's Leggings", Material.GOLD_LEGGINGS, 
			new String[]{min+"30%"+lightning, plus+"30%"+water}),
			new DStat[]{new DStat(false, 30, DamageType.LIGHTNING), new DStat(true, 30, DamageType.WATER)}),
	LOOSERS_PANTS(new NamedStack(ChatColor.GRAY+"Loosers Pants", Material.LEATHER_LEGGINGS, Color.fromBGR(60, 60, 60), 
			new String[]{plus+"15%"+lightning, plus+"15%"+fire, plus+"10%"+light, min+"40%"+dark}),
			new DStat[]{new DStat(true, 15, DamageType.LIGHTNING), new DStat(true, 15, DamageType.FIRE), new DStat(true, 10, DamageType.LIGHT), new DStat(false, 40, DamageType.DARK)},
			true),	
	ARCHERS_PYJAMAS(new NamedStack(ChatColor.GRAY+"Archer's Pyjamas", Material.LEATHER_LEGGINGS, Color.GRAY,
			new String[]{min+"30%"+ground, plus+"30%"+fire}), 
			new DStat[]{new DStat(false, 30, DamageType.GROUND), new DStat(true, 30, DamageType.FIRE)}, 
			true);
	

ItemStack s = null;
List<DStat> stats = null;
boolean unlockable = false;
boolean findable = false;

pants(ItemStack stack, DStat[] stats){
	s = stack;
this.stats = Arrays.asList(stats);
}

pants(ItemStack stack, DStat[] stats , boolean un){
	s = stack;
this.stats = Arrays.asList(stats);
unlockable = un;
}

pants(ItemStack stack, DStat[] stats , boolean un, boolean fn){
	s = stack;
this.stats = Arrays.asList(stats);
unlockable = un;
findable = fn;
}



public ItemStack getStack(){
	return s;
}

public boolean isUnlockable(){
	return unlockable;
}

public boolean isFindable(){
	return findable;
}

public List<DStat> getStats(){
	return stats;
}

public static pants getFromStack(ItemStack stack){
	for(pants h : pants.values()){
		if(stack.getItemMeta().getDisplayName().contentEquals(h.getStack().getItemMeta().getDisplayName())){
			return h;
		}
	}
	return null;
}

}

public enum boots{
	
	
	
	RUNNER_SHOES(new NamedStack(ChatColor.WHITE+"Runner's Shoes", Material.LEATHER_BOOTS, Color.WHITE,
			new String[]{min+"20%"+water, plus+"20%"+lightning}), 
			new DStat[]{new DStat(false, 20, DamageType.WATER), new DStat(true, 20, DamageType.LIGHTNING)}),
	UGLIES(new NamedStack(ChatColor.GOLD+"Uglies", Material.LEATHER_BOOTS, Color.NAVY,
			new String[]{min+"15%"+ground, min+"15%"+water, plus+"30%"+light,}),
			new DStat[]{new DStat(false, 15, DamageType.GROUND), new DStat(false, 15, DamageType.WATER), new DStat(true, 30, DamageType.LIGHT)}),
	PANTSER_BOOTS(new NamedStack(ChatColor.GRAY+"Pantser Boots", Material.IRON_BOOTS, 
			new String[]{min+"50%"+ground,  plus+"50%"+lightning,}),
			new DStat[]{new DStat(false, 50, DamageType.GROUND),  new DStat(true, 50, DamageType.LIGHTNING)},
			true, true),
	WINDFUR_SANDALES(new NamedStack(ChatColor.WHITE+"Windfur Sandales", Material.LEATHER_BOOTS, Color.fromBGR(255, 250, 240), 
			new String[]{min+"30%"+ground,  min+"25%"+air, plus+"35%"+lightning, plus+"20%"+fire}),
			new DStat[]{new DStat(false, 30, DamageType.GROUND),  new DStat(false, 25, DamageType.AIR), new DStat(true, 35, DamageType.LIGHTNING), new DStat(true, 20, DamageType.FIRE)},
			true, true);
	

ItemStack s = null;
List<DStat> stats = null;
boolean unlockable = false;
boolean findable = false;

boots(ItemStack stack, DStat[] stats){
	s = stack;
this.stats = Arrays.asList(stats);
}

boots(ItemStack stack, DStat[] stats , boolean un){
	s = stack;
this.stats = Arrays.asList(stats);
unlockable = un;

}

boots(ItemStack stack, DStat[] stats , boolean un, boolean fn){
	s = stack;
this.stats = Arrays.asList(stats);
unlockable = un;
findable = fn;
}

public ItemStack getStack(){
	return s;
}

public boolean isUnlockable(){
	return unlockable;
}

public boolean isFindable(){
	return findable;
}

public List<DStat> getStats(){
	return stats;
}

public static boots getFromStack(ItemStack stack){
	for(boots h : boots.values()){
		if(stack.getItemMeta().getDisplayName().contentEquals(h.getStack().getItemMeta().getDisplayName())){
			return h;
		}
	}
	return null;
}

}
	

	
}
