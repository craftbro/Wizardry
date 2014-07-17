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
		cost = 60;
		
		des.add(ChatColor.DARK_AQUA+"Shoots a trail of hot water for "+ChatColor.WHITE+"2"+ChatColor.DARK_AQUA+" second that");
		des.add(ChatColor.DARK_AQUA+"deals "+ChatColor.WHITE+"5"+ChatColor.AQUA+" Water "+ChatColor.DARK_AQUA+"damage");
		des.add(ChatColor.DARK_AQUA+"and"+ChatColor.RED+" Burns"+ChatColor.DARK_AQUA+" the emeny for "+ChatColor.WHITE+"6"+ChatColor.DARK_AQUA+" seconds");

		info.put("Range", ChatColor.GREEN+"1.5");
		
		rem.add(Condition.BURN.getReminder());
		
		slot = SpellSlot.PRIMARY_STICK;
	}

	
	@Override
	public void cast(){
		final FireworkEffect effect = FireworkEffect.builder().withColor(Color.fromBGR(255, 155, 0)).withFade(Color.fromBGR(255, 155, 100)).with(Type.BALL).trail(true).build();
		
		new BukkitRunnable(){
			int times = 0;
			List<LivingEntity> safes = new ArrayList<LivingEntity>();
			double length = 0;
			public void run(){
				List<LivingEntity> ticksafe = new ArrayList<LivingEntity>();
				Location loc = p.getEyeLocation();
				Vector vec = p.getLocation().getDirection().normalize().multiply(0.5);
				p.getWorld().playSound(p.getLocation(), Sound.FIZZ, 0.1F, 2);
				
				for(int i=0; i<length; i++){
					loc.add(vec);
						ParticleEffect.SPLASH.animateAtLocation(loc, 5, 1);
						if (times % 2 == 0){
							ParticleEffect.LAVA.animateAtLocation(loc, 1, 1);
							
							for (LivingEntity e : loc.getWorld().getLivingEntities()) if(e.getEyeLocation().distance(loc) <= 1.5){
								if (!BasicUtil.isInTeam(e, p) && !ticksafe.contains(e)){
									BasicUtil.damage(e, p, 5, DamageType.WATER);
									ticksafe.add(e);
									if (times % 5 == 0){
										try {
											CodeEffect.playFirework(p.getWorld(), e.getLocation(), effect);
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										}
									if(!safes.contains(e)){
										BasicUtil.giveCondtition(e, Condition.BURN, 3);
										safes.add(e);
										}
									}
							}
						}
					}
				if (length + 2.5 < 16){
					length += 2.5;
				} else {
					length = 16;
				}
				
				if (times >= 20){
					cancel();
				}
				times++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
		
	}
}
