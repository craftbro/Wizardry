package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;

public class SpellStarBomb extends Spell{

	public SpellStarBomb(Player p) {
		super(p);
		
		name = ChatColor.RED+"Fire Bomb";
		stack = new ItemStack(Material.BLAZE_POWDER);
		cost = 50;
		
		des.add(ChatColor.DARK_AQUA+"Fires a bomb the will explode after");
		des.add(ChatColor.WHITE+"0.5"+ChatColor.DARK_AQUA+" seconds, burning and dealing");
		des.add(ChatColor.WHITE+"50 "+ChatColor.RED+"Fire "+ChatColor.DARK_AQUA+"damage to enemies");
		
		info.put("Range", ChatColor.GREEN+"3");
		info.put("Burn Duration", ChatColor.AQUA+"10 seconds");
		
		rem.add(Condition.BURN.getReminder());
		
		slot = SpellSlot.PRIMARY_STICK;
	}
	
	@Override
	public void cast(){
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.getMaterial(402)), 10);
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 3)){
					if(!BasicUtil.isInTeam(e, p)){ BasicUtil.damage(e, p, 50, DamageType.FIRE); BasicUtil.giveCondtition(e, Condition.BURN, 5);}
				}
				
				FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).withFade(Color.ORANGE).with(Type.BALL).build();
				
				try {
					CodeEffect.playFirework(item.getWorld(), item.getLocation(), effect);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.BAT_TAKEOFF, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(2));
		
	}
	
	

}
