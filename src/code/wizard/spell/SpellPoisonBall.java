package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import code.wizard.effect.ParticleEffect;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.ProjectileBall;

public class SpellPoisonBall extends Spell{

	public SpellPoisonBall(Player p) {
		super(p);
		
		name = ChatColor.GREEN+"Venomous Slingshot";
		stack = new ItemStack(Material.INK_SACK, 1, (byte)2);
		cost = 30;
		
		des.add(ChatColor.DARK_AQUA+"Fires a poisonous substance");
		des.add(ChatColor.DARK_AQUA+"that, on impact, "+ChatColor.DARK_GREEN+"Poisons "+ChatColor.DARK_AQUA+"enemies");
		
		info.put("Range", ChatColor.GREEN+"5");
		info.put("Poison Duration", ChatColor.AQUA+"6 seconds");
		
		rem.add(Condition.POISON.getReminder());
		
		slot = SpellSlot.PRIMARY_MELEE;
	}
	
	@Override
	public void cast(){
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.INK_SACK, 1, (byte)2));
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 5)){
					if(!BasicUtil.isInTeam(e, p))BasicUtil.giveCondtition(e, Condition.POISON, 3);
				}
				
				item.getWorld().playSound(item.getLocation(), Sound.SLIME_ATTACK, 5, -2);
				
				BasicUtil.playEffectInCircle(item.getLocation(), 5, ParticleEffect.MAGIC_CRIT, 2);
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.SLIME_WALK2, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(1.6));
		
	}
	
	

}
