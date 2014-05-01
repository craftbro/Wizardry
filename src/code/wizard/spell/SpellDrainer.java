package code.wizard.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
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
import code.wizard.util.Condition;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;

public class SpellDrainer extends Spell{

	public SpellDrainer(Player p) {
		super(p);
		
		name = ChatColor.RED+"Drain Ball";
		stack = new ItemStack(Material.DIAMOND);
		cost = 100;
		
		des.add(ChatColor.DARK_AQUA+"Fires a gem that, for 4 seconds steals health");
		des.add(ChatColor.DARK_AQUA+"from surrounding enemies every second");
		
		info.put("Range", ChatColor.GREEN+"4");
		info.put("Damage", ChatColor.GREEN+"30");
		info.put("Cooldown", ChatColor.AQUA+"40 seconds");
		
	
		
		slot = SpellSlot.SECUNDAIRY_WAND;
	}
	
	private void sendHealRay(final int health, final Location l){
		new BukkitRunnable(){
			int times = 0;
			List<LivingEntity> safes = new ArrayList<LivingEntity>();
			double length = 1;
			
			Location loc = l;
				
			
			public void run(){
					List<LivingEntity> ticksafe = new ArrayList<LivingEntity>();
					
					
					Vector vec = p.getEyeLocation().toVector().subtract(loc.toVector()).normalize().multiply(0.5);	

					
					for(int i=0; i<length; i++){
					loc.add(vec);
					if(loc.distance(p.getEyeLocation()) <= 1){
						loc.getWorld().playSound(loc, Sound.FIZZ, 2, 2);
						ParticleEffect.WITCH_MAGIC.animateAtLocation(loc, 3, 1);
						cancel();
						KitManager.getKit(p).heal(health);
					}else if (loc.getBlock().getType().isSolid()){
						loc.getWorld().playSound(loc, Sound.FIZZ, 2, 2);
						ParticleEffect.WITCH_MAGIC.animateAtLocation(loc, 3, 1);
						cancel();
					} else {
						ParticleEffect.HEART.animateAtLocation(loc, 5, 1);
					}
					}
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 2);
	}
	
	@Override
	public void cast(){
		
		cooldown = 40;
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.DIAMOND));
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				final Item item = ball.getItem();
				
			
			new BukkitRunnable(){
				
				int times = 4;

				@Override
				public void run() {
					if(times >= 0){
					int heal = 0;
					
					for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 4)){
						if(BasicUtil.isInTeam(e, p)) continue;
						heal+=BasicUtil.damage(e, p, 30, DamageType.POISON);
						
					}
					
					if(heal > 0) sendHealRay(heal, item.getLocation());
					
					BasicUtil.playEffectInCircle(item.getLocation(), 6, ParticleEffect.WITCH_MAGIC, 2);
					p.getWorld().playSound(item.getLocation(), Sound.CAT_HISS, 4, 2);
					
					times--;
					}else{
						item.remove();
						cancel();
					}
				}
				
			}.runTaskTimer(Main.getInstance(), 1, 20);
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.SLIME_WALK2, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(1.6));
		
	}
	
	

}
