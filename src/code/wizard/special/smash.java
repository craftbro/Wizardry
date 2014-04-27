package code.wizard.special;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;

public class smash implements Listener {

	
	Sheep s = null;
	BukkitRunnable task;
	int health = 5;
	boolean dead = false;
	
	public void spawn(Location loc){
		s = (Sheep) loc.getWorld().spawnEntity(loc, EntityType.SHEEP);
		
		s.setCustomName("jeb_");
		s.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 2));
		
		
		startUpdate();
		
	Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	Bukkit.broadcastMessage(Main.getPrefix()+"The "+Condition.SUPER.getName()+ChatColor.DARK_GREEN+" Sheep spawned! Get it to become "+Condition.SUPER.getName());
	Bukkit.broadcastMessage(Main.getPrefix()+Condition.SUPER.getReminder());
	}
	
	@EventHandler
	public void hit(EntityDamageByEntityEvent event){
		if(s != null && event.getEntity() == s && event.getDamager() instanceof Player){
			Player p = (Player)event.getDamager();			
			event.setCancelled(true);
			
			health--;
			
			if(!dead){
			if(health > 0){
				s.getWorld().playSound(s.getEyeLocation(), Sound.IRONGOLEM_HIT, 4, 3);
				ParticleEffect.CRIT.animateAtLocation(s.getEyeLocation(), 10, 1);
			}else{
				dead = true;
				s.getWorld().playSound(s.getEyeLocation(), Sound.GLASS, 4, 0);
				
				Bukkit.broadcastMessage(Main.getPrefix()+p.getName()+" got the "+Condition.SUPER.getName()+" sheep!");
				BasicUtil.giveCondtition(p, Condition.SUPER, 10);
				
				s.remove();
				task.cancel();
				
			}
			}
			
		}
	}
	
	
	private void startUpdate(){
		task =  new BukkitRunnable(){

			@Override
			public void run() {
				if(s.isDead()){s = null; cancel();}
				
			Random r = new Random();
			
			double x = ((r.nextDouble()*10)-5)/10;
			double z = ((r.nextDouble()*10)-5)/10;
			
			s.setVelocity(new Vector(x, 0.35, z));
			
		
			}
			
		};
		task.runTaskTimer(Main.getInstance(), 1, 10);
	}
	
	
}
