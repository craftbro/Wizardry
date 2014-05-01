package code.wizard.spell;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import code.wizard.main.Main;

public class SpellLoader {

	
	public static HashMap<String, Spell> spells = new HashMap();
	
	
	public static void initialize(){
		//spells.put("Venomous Slingshot", new SpellPoisonBall(null));
		spells.put("Coal Bomb", new SpellCoalBomb(null));
		spells.put("Char Charge", new SpellCharCharge(null));
		spells.put("Flint Shot", new SpellFlintShot(null));
		spells.put("Fire Bomb", new SpellStarBomb(null));
		spells.put("Victory Bomb", new SpellVictoryBomb(null));
		spells.put("Thunder Rain", new SpellThunderRain(null));
		spells.put("Uppercut", new SpellUppercut(null));
		spells.put("Holy Fountain", new SpellHolyFountain(null));
		spells.put("Flash Bomb", new SpellFlashBomb(null));
		spells.put("Sticky Substance", new SpellSlimeBall(null));
		spells.put("Recover", new SpellRecover(null));
		spells.put("Leap", new SpellLeap(null));
		spells.put("Star Bomb", new SpellBombStar(null));
		spells.put("Magical Gem", new SpellMagicalGem(null));
		spells.put("Clayball", new SpellClayBall(null));
		spells.put("Levitate", new SpellLevitate(null));
		spells.put("Hot Water", new SpellHotWater(null));
		spells.put("Compresion Blast", new SpellCompresionBlast(null));
		spells.put("Virtual Bolt", new SpellVBolt(null));
		spells.put("Bouncy Sun", new SpellBouncySun(null));
		spells.put("Draining Blow", new SpellDrainBlow(null));
		spells.put("Fire Blast", new SpellFireBlast(null));
		spells.put("Drain Ball", new SpellDrainer(null));
		spells.put("Sunny Day", new SpellSunnyDay(null));
	//	spells.put("Roots", new SpellRoot(null));
		//spells.put("Mana Sponge", new SpellManaDrain(null));
	}
	
	
	public static Spell loadSpell(Player p, String s){
		Spell sp = spells.get(s);
		Spell spell = null;
		
		try{
			spell = (Spell) sp.clone();
			spell.setPlayer(p);
		}catch(Exception e){
			Main.print("Error loading spell '"+s+"':");
			e.printStackTrace();
		}
		
		return spell;
	}
	

	public static Spell loadSpell(Player p, ItemStack i){
		String s = ChatColor.stripColor(i.getItemMeta().getDisplayName());
		
		return loadSpell(p, s);
	}
	
}
