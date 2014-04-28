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
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

import code.wizard.effect.CodeEffect;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;
import code.wizard.util.ProjectileWrapper;

public class SpellFlashBomb extends Spell{

	public SpellFlashBomb(Player p) {
		super(p);
		
		name = ChatColor.GRAY+"Flash Bomb";
		stack = new ItemStack(Material.GLOWSTONE_DUST);
		cost = 60;
		
		des.add(ChatColor.DARK_AQUA+"Fires a bomb that explodes after");
		des.add(ChatColor.WHITE+"0.25 "+ChatColor.DARK_AQUA+"seconds, weakening enemies");
		
		
		info.put("Range", ChatColor.GREEN+"3");
		info.put("Weakness Duration", ChatColor.AQUA+"12 seconds");
		
		rem.add(Condition.WEAK.getReminder());
		
		slot = SpellSlot.SECUNDAIRY_STICK;
		}
	
	@Override
	public void cast(){
final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.GLOWSTONE_DUST), 5);
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 3)){
					if(!BasicUtil.isInTeam(e, p)){  BasicUtil.giveCondtition(e, Condition.WEAK, 6);}
				}
				
				FireworkEffect effect = FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.ORANGE).with(Type.BALL).build();
				
				try {
					CodeEffect.playFirework(item.getWorld(), item.getLocation(), effect);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(2));
	}
	
	

}
