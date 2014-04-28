package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.player.KitManager;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.ProjectileBall;

public class SpellRecover extends Spell{

	public SpellRecover(Player p) {
		super(p);
		
		name = ChatColor.AQUA+"Recover";
		stack = new ItemStack(Material.GOLDEN_APPLE);
		cost = 100;
		
		des.add(ChatColor.DARK_AQUA+"Heals you 150 health, but");
		des.add(ChatColor.DARK_AQUA+"also "+ChatColor.GRAY+"Weakens "+ChatColor.DARK_AQUA+"and "+ChatColor.DARK_RED+"Oils "+ChatColor.DARK_AQUA+"you");
		
	
		info.put("Weakness Duration", ChatColor.AQUA+"6 seconds");
		info.put("Oil Duration", ChatColor.AQUA+"20 seconds");
		
		rem.add(Condition.WEAK.getReminder());
		rem.add(Condition.OIL.getReminder());
		
		
		slot = SpellSlot.SECUNDAIRY_WAND;
	}
	
	@Override
	public void cast(){
		
		KitManager.getKit(p).heal(150);
		BasicUtil.giveCondtition(p, Condition.WEAK, 3);
		BasicUtil.giveCondtition(p, Condition.OIL, 20);
		
		FireworkEffect effect = FireworkEffect.builder().withColor(Color.AQUA).withFade(Color.BLUE).with(Type.BALL).build();
		
		try {
			CodeEffect.playFirework(p.getWorld(), p.getEyeLocation(), effect);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	

}
