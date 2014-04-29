package code.wizard.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;

public class SpellCompresionBlast extends Spell{

	public SpellCompresionBlast(Player p) {
		super(p);
		
		name = ChatColor.WHITE+"Compresion Blast";
		stack = new ItemStack(Material.INK_SACK, 1, (byte)15);
		cost = 80;
		
		unlockable = true;
		findable = true;
		
		des.add(ChatColor.DARK_AQUA+"Shoots a conrtolable trail of air that,");
		des.add(ChatColor.DARK_AQUA+"on impact, deals "+ChatColor.WHITE+"100 Air"+ChatColor.DARK_AQUA+"damage to enemies");
	

		
		slot = SpellSlot.PRIMARY_WAND;
	}

	
	@Override
	public void cast(){
		final FireworkEffect effect = FireworkEffect.builder().withColor(Color.WHITE).with(Type.BALL).trail(true).build();
		
		new BukkitRunnable(){
			int times = 0;
		
			int length = 2;
			
			boolean end = false;
			Location loc = p.getEyeLocation();
			public void run(){
					Vector vec = p.getLocation().getDirection().normalize().multiply(0.4);
					
					
					
					for(int i=0; i<length; i++){
						if(!end){
					loc.add(vec);
					if (loc.getBlock().getType().isSolid()){
						end = true;
						try {
							CodeEffect.playFirework(p.getWorld(), loc, effect);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
						for (LivingEntity e : BasicUtil.getInRadius(loc, 2)){
							if (!BasicUtil.isInTeam(e, p)){
								BasicUtil.damage(e, p, 100, DamageType.AIR);
						}
						}
						cancel();
					} else {
						ParticleEffect.SPELL.animateAtLocation(loc, 8, 1);
					
						
					}
					
					}
					}
				
				if (times >= 200){
					cancel();
				}
				times++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		
		p.getWorld().playSound(p.getLocation(), Sound.FIZZ, 0.1F, 2);
	}
}
