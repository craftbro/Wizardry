package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
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

public class SpellClayBall extends Spell{

	public SpellClayBall(Player p) {
		super(p);
		
		name = ChatColor.GRAY+"Clayball";
		stack = new ItemStack(Material.CLAY_BALL);
		cost = 60;
		
		unlockable = true;
		findable = true;
		
		des.add(ChatColor.DARK_AQUA+"Fires a ball of clay");
		des.add(ChatColor.DARK_AQUA+"that, on impact, "+ChatColor.GRAY+"Tires "+ChatColor.DARK_AQUA+"enemies");
		
		info.put("Range", ChatColor.GREEN+"5");
		info.put("Tired Duration", ChatColor.AQUA+"8 seconds");
		
		rem.add(Condition.TIRED.getReminder());
		
		slot = SpellSlot.SECUNDAIRY_STICK;
	}
	
	@Override
	public void cast(){
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.CLAY_BALL));
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 5)){
					if(!BasicUtil.isInTeam(e, p))BasicUtil.giveCondtition(e, Condition.TIRED, 4);
				}
				
				item.getWorld().playSound(item.getLocation(), Sound.MAGMACUBE_JUMP, 5, 2);
				
				BasicUtil.playEffectInCircle(item.getLocation().subtract(0, 1, 0), 5, Effect.STEP_SOUND, Material.CLAY.getId());
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.SLIME_WALK2, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(1.6));
		
	}
	
	

}
