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

public class SpellBombStar extends Spell{

	public SpellBombStar(Player p) {
		super(p);
		
		name = ChatColor.AQUA+"Star Bomb";
		stack = new ItemStack(Material.NETHER_STAR);
		cost = 50;
		
		
		
		des.add(ChatColor.DARK_AQUA+"Fires a star the will explode after");
		des.add(ChatColor.WHITE+"0.25"+ChatColor.DARK_AQUA+" seconds, "+ChatColor.DARK_RED+"Oiling"+ChatColor.DARK_AQUA+" and dealing");
		des.add(ChatColor.WHITE+"60 "+ChatColor.GOLD+"Light "+ChatColor.DARK_AQUA+"damage to enemies");
		
		info.put("Range", ChatColor.GREEN+"3");
		info.put("Oil Duration", ChatColor.AQUA+"20 seconds");
		
		rem.add(Condition.OIL.getReminder());
		
		slot = SpellSlot.PRIMARY_STICK;
	}
	
	@Override
	public void cast(){
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.NETHER_STAR), 5);
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 3)){
					if(!BasicUtil.isInTeam(e, p)){ BasicUtil.damage(e, p, 60, DamageType.LIGHT); BasicUtil.giveCondtition(e, Condition.OIL, 10);}
				}
				
				FireworkEffect effect = FireworkEffect.builder().withColor(Color.AQUA).withFade(Color.LIME).with(Type.STAR).build();
				
				try {
					CodeEffect.playFirework(item.getWorld(), item.getLocation(), effect);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_TWINKLE, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(2));
		
	}
	
	

}
