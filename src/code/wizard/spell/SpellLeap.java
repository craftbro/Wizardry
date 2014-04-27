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
import org.bukkit.util.Vector;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.ProjectileBall;

public class SpellLeap extends Spell{

	public SpellLeap(Player p) {
		super(p);
		
		name = ChatColor.WHITE+"Leap";
		stack = new ItemStack(Material.ARROW);
		cost = 45;
		
		des.add(ChatColor.DARK_AQUA+"Makes you leap forward");
		
		//info.put("Jump Height", "13 Blocks");
		info.put(ChatColor.RED+"Note", "Cannot be uses while in the air");
		
		slot = SpellSlot.SECUNDAIRY_MELEE;
	}
	
	@Override
	public void use(){
		float xp = p.getExp();
		float xpCost = cost/100;
		
		if(xp >= xpCost && p.isOnGround()){
			p.setExp(xp - xpCost);
			cast();
		}
	}
	
	@Override
	public void cast(){
		
		Vector v = p.getLocation().getDirection().multiply(2);
		
		v.add(new Vector(0, 0.5, 0));
		
	p.setVelocity(v);
	
	p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 5, 3);
	
	FireworkEffect effect = FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.ORANGE).with(Type.BURST).build();
	
	try {
		CodeEffect.playFirework(p.getWorld(), p.getLocation(), effect);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		
	}
	
	

}
