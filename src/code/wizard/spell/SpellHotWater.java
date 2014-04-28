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

public class SpellHotWater extends Spell{

	public SpellHotWater(Player p) {
		super(p);
		
		name = ChatColor.RED+"Hot Water";
		stack = new ItemStack(Material.LAVA_BUCKET);
		cost = 65;
		
		des.add(ChatColor.DARK_AQUA+"Shoots a trail of hot water for "+ChatColor.WHITE+"2"+ChatColor.DARK_AQUA+" that");
		des.add(ChatColor.DARK_AQUA+"deals "+ChatColor.WHITE+"5"+ChatColor.AQUA+" Water "+ChatColor.DARK_AQUA+"damage");
		des.add(ChatColor.DARK_AQUA+"and"+ChatColor.RED+" Burns"+ChatColor.AQUA+" the emeny for "+ChatColor.WHITE+"4"+ChatColor.DARK_AQUA+"seconds");

		info.put("Range", ChatColor.GREEN+"2");
		info.put("Distance", ChatColor.GREEN+"6");
		
		rem.add(Condition.BURN.getReminder());
		
		slot = SpellSlot.PRIMARY_STICK;
	}

	
	@Override
	public void cast(){
		final FireworkEffect effect = FireworkEffect.builder().withColor(Color.fromBGR(255, 155, 0)).withFade(Color.fromBGR(255, 155, 100)).with(Type.BALL).trail(true).build();
		
		new BukkitRunnable(){
			int times = 0;
			Vector vec = p.getLocation().getDirection().normalize().multiply(0.4);
			Location loc = p.getEyeLocation();
			List<LivingEntity> safes = new ArrayList<LivingEntity>();
			public void run(){
				if (times % 2 == 0){
					
					for(int i=0; i<8; i++){
					loc.add(vec);
					ParticleEffect.SPLASH.animateAtLocation(loc, 2, 1);
					for (LivingEntity e : BasicUtil.getInRadius(loc, 2)){
						if (!BasicUtil.isInTeam(e, p)){
							BasicUtil.damage(e, p, 5, DamageType.WATER);
						if(!safes.contains(e))	BasicUtil.giveCondtition(e, Condition.BURN, 2/*2 here = 4 seconds... right?*/);
							safes.add(e);
						
							try {
								CodeEffect.playFirework(p.getWorld(), e.getLocation(), effect);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					}
				}
				
				if (times >= 40){
					cancel();
				}
				times++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
		p.getWorld().playSound(p.getLocation(), Sound.FIZZ, 0.1F, 2);
	}
}