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
import code.wizard.player.KitManager;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;

public class SpellHolyFountain extends Spell{

	public SpellHolyFountain(Player p) {
		super(p);
		
		name = ChatColor.AQUA+"Holy Fountain";
		stack = new ItemStack(Material.WATER_BUCKET);
		cost = 100;
		
		des.add(ChatColor.DARK_AQUA+"Fires an ancient relic that, on");
		des.add(ChatColor.DARK_AQUA+"impact, spawns a fountain dealing");
		des.add(ChatColor.WHITE+"60 "+ChatColor.AQUA+"Water "+ChatColor.DARK_AQUA+"damage to enemies and");
		des.add(ChatColor.DARK_AQUA+"heals you teh damage you dealt with every hit");
		
		info.put("Range", ChatColor.GREEN+"5");
		
		slot = SpellSlot.SECUNDAIRY_WAND;
	}
	
	@Override
	public void cast(){
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.INK_SACK, 1, (byte)6));
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				item.getWorld().playSound(item.getLocation(), Sound.WATER, 5, -2);
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 5)){
					if(!BasicUtil.isInTeam(e, p)){  KitManager.getKit(p).heal(BasicUtil.damage(e, p, 60, DamageType.WATER));}
				}
				
				FireworkEffect effect = FireworkEffect.builder().withColor(Color.AQUA).withFade(Color.BLUE).with(Type.BALL_LARGE).build();
				
				
				
				try {
					CodeEffect.playFirework(item.getWorld(), item.getLocation(), effect);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				BasicUtil.playEffectInCircle(item.getLocation(), 5, ParticleEffect.SPLASH, 2);
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.SPLASH, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(2));
		
	}
	
	

}
