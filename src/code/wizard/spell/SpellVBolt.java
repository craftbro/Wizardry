package code.wizard.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;

public class SpellVBolt extends Spell{

	public SpellVBolt(Player p) {
		super(p);
		
		name = ChatColor.AQUA+"Virtual Bolt";
		stack = new ItemStack(Material.DIAMOND_BLOCK);
		cost = 75;
		
		des.add(ChatColor.DARK_AQUA+"Shoots a virtual bolt that");
		des.add(ChatColor.DARK_AQUA+"deals "+ChatColor.WHITE+"75"+ChatColor.GREEN+" Ground"+ChatColor.DARK_AQUA+" damage on impact");
		des.add(ChatColor.DARK_AQUA+"and damage nearby emenies "+ChatColor.WHITE+"15"+ChatColor.GREEN+" Ground damage");

		info.put("Primary Range", "2");
		info.put("Nearby Range", "5");
		
		unlockable = true;
		findable = true;
		
		slot = SpellSlot.PRIMARY_WAND;
	}
	
	@Override
	public void cast(){

		new BukkitRunnable(){
			final Hologram bolt = HoloAPI.getManager().createSimpleHologram(p.getLocation(), 380 / 19, new String[]{
				ChatColor.AQUA+"█"
			});
			Vector vec = p.getLocation().getDirection().normalize().multiply(0.1);
			int times = 0;
			Location currLoc = p.getEyeLocation();
			public void run(){
				if (currLoc.getBlock().getType().isSolid()){
					cancel();
					try {
						HoloAPI.getManager().stopTracking(bolt);
						bolt.clearAllPlayerViews();
					} catch (Exception e){
						e.printStackTrace();
					}
					ParticleEffect.ANGRY_VILLAGER.animateAtLocation(currLoc, 1, 1);
					currLoc.getWorld().playSound(currLoc, Sound.CHICKEN_EGG_POP, 4, 0);
				} else {
					if (Math.abs(vec.getX()) < 7.5 && Math.abs(vec.getY()) < 7.5 && Math.abs(vec.getZ()) < 7.5){
						vec.multiply(1.09);
					}
					currLoc = currLoc.add(vec);
					bolt.move(currLoc);
					List<LivingEntity> list = new ArrayList<LivingEntity>();
					for (LivingEntity e : currLoc.getWorld().getLivingEntities()) if(e.getEyeLocation().distance(currLoc) <= 2){
						if (!BasicUtil.isInTeam(e, p)){
							list.add(e);
						}
					}
					if (!list.isEmpty()){
						for (LivingEntity e : list){
							BasicUtil.damage(e, p, 85, DamageType.GROUND);
						}
						List<LivingEntity> list2 = new ArrayList<LivingEntity>(list);
						for (LivingEntity e : currLoc.getWorld().getLivingEntities()) if(e.getEyeLocation().distance(currLoc) <= 5){
							list2.add(e);
						}
						list2.removeAll(list);
						for (LivingEntity e : list2){
							if (!BasicUtil.isInTeam(e, p)){
								BasicUtil.damage(e, p, 20, DamageType.GROUND);
							}
						}
						cancel();
						ParticleEffect.LARGE_EXPLODE.animateAtLocation(currLoc, 5, 1);
						currLoc.getWorld().playSound(currLoc, Sound.CHICKEN_EGG_POP, 4, 1);
						try {
							HoloAPI.getManager().stopTracking(bolt);
							bolt.clearAllPlayerViews();
						} catch (Exception e){
							
						}
					} else {
						if (times >= 360){
							cancel();
							ParticleEffect.LARGE_EXPLODE.animateAtLocation(currLoc, 5, 1);
							currLoc.getWorld().playSound(currLoc, Sound.CHICKEN_EGG_POP, 4, 1);
							try {
								HoloAPI.getManager().stopTracking(bolt);
								bolt.clearAllPlayerViews();
							} catch (Exception e){
								
							}
						}
						times++;
					}
				}
			}
		}.runTaskTimer(Main.getInstance(), 1, 1);
	
		p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 5, 1);
	
		FireworkEffect effect = FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.fromBGR(225, 225, 255)).with(Type.BURST).build();
	
		try {
			CodeEffect.playFirework(p.getWorld(), p.getLocation(), effect);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
