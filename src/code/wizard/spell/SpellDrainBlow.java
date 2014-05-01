package code.wizard.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.player.KitManager;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;

public class SpellDrainBlow extends Spell{

	public SpellDrainBlow(Player p) {
		super(p);
		
		name = ChatColor.GREEN+"Draining Blow";
		stack = new ItemStack(Material.QUARTZ);
		cost = 75;
		
		des.add(ChatColor.DARK_AQUA+"Blows out a wind that drain life");
		des.add(ChatColor.DARK_AQUA+"if it hits anything it damages them "+ChatColor.WHITE+"65 Air"+ChatColor.DARK_AQUA+" damage");
		des.add(ChatColor.DARK_AQUA+"when it hits it flies back with the drained life");

		info.put("Range", ChatColor.GREEN+"2.5");
		
		slot = SpellSlot.SECUNDAIRY_WAND;
	}

	
	@Override
	public void cast(){
		Builder b = FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.LIME).withTrail();
		final FireworkEffect effect = b.with(Type.BALL).build();
		final FireworkEffect effectB = b.with(Type.BURST).build();

		new BukkitRunnable(){
			public Location currLoc = p.getEyeLocation();
			public Vector vec = currLoc.getDirection().normalize().multiply(1.05);
			public boolean rainbow = false;
			public int damage;
			int times = 0;
			public void run(){
				if (!rainbow){
					currLoc.add(vec);
					vec.multiply(0.9475);
					if (currLoc.getBlock().getType().isSolid() || Math.abs(vec.getX()) <= 0.008 && Math.abs(vec.getY()) <= 0.008 && Math.abs(vec.getZ()) <= 0.008){
						cancel();
						try{
							CodeEffect.playFirework(currLoc, effectB);
						}catch(Exception e1){
							e1.printStackTrace();
						}
					} else {
						ParticleEffect.FIREWORK_SPARK.animateAtLocation(currLoc, 2, 0);
						List<LivingEntity> list = new ArrayList<LivingEntity>(); for (LivingEntity e : currLoc.getWorld().getLivingEntities()) if(e.getEyeLocation().distance(currLoc) <= 2.5 && !BasicUtil.isInTeam(e, p)) list.add(e);
						if (!list.isEmpty()){
							try{
								CodeEffect.playFirework(currLoc, effect);
							}catch(Exception e1){
								e1.printStackTrace();
							}
							for (LivingEntity e : list){
								damage += BasicUtil.damage(e, p, 65, DamageType.AIR);
							}
							rainbow = true;
							times = 0;
						} else if (times >= 380){
							cancel();
							try{
								CodeEffect.playFirework(currLoc, effectB);
							}catch(Exception e1){
								e1.printStackTrace();
							}
						}
					}
					times++;
				} else {
					vec = p.getEyeLocation().toVector().subtract(currLoc.toVector()).normalize().multiply(0.2);
					currLoc.add(vec);
					ParticleEffect.RED_DUST.animateAtLocation(currLoc, 2, 1);
					List<LivingEntity> list = new ArrayList<LivingEntity>(); for (LivingEntity e : currLoc.getWorld().getLivingEntities()) if(e.getEyeLocation().distance(currLoc) <= 2.5 && BasicUtil.isInTeam(e, p)) list.add(e);
					if (list.contains(p)){
						KitManager.getKit(p).heal(damage);
						cancel();
						try{
							CodeEffect.playFirework(currLoc, effectB);
						}catch(Exception e1){
							e1.printStackTrace();
						}
					} else {
						if (times >= 540){
							cancel();
							try{
								CodeEffect.playFirework(currLoc, effectB);
							}catch(Exception e1){
								e1.printStackTrace();
							}
						}
					}
					times++;
				}
			}
		}.runTaskTimer(Main.getInstance(), 1, 1);
	}
}
