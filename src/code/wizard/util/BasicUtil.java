package code.wizard.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.player.Kit;
import code.wizard.player.KitManager;

import com.dsh105.holoapi.HoloAPI;

public class BasicUtil {

	/**
     * Returns all LivingEntity in a certain radius of a center point
     * @param l The center location
     * @param radius The radius
     */
	public static List<LivingEntity> getInRadius(Location l, double radius){
		List<LivingEntity> near = new ArrayList<LivingEntity>();
		
		for(LivingEntity e : l.getWorld().getLivingEntities()) if(e.getLocation().distance(l) <= radius) near.add(e);
		
		return near;		
	}
	
	/**
     * Returns a list of 360 Locations, froming a circle
      * @param l The center location
     * @param radius The radius
     */
public static List<Location> getCircle(Location l, double radius){

		
		List<Location> locs = new ArrayList<Location>();
		
	
		
		for(int t=0; t<=360; t++){	
			
			double x = radius*Math.cos(t) + l.getX();
			double z = radius*Math.sin(t) + l.getZ();
			
			
			locs.add(new Location(l.getWorld(), x, l.getY(), z));
		}
		
		return locs;
}
	
 /**
 * Damages a Entity, returning the damage done
  * @param e The Entity that shoudl be damaged
  * @param d The damageing Player (may be null)
  * @param damage The damage that should be dealt
  * @param type The type of damage
  */
	public static double damage(LivingEntity e, Player d, int damage, DamageType type){
		return damage(e, d, damage, type, true);
	}

	/**
	 * Returns the percent of a certain number
	  * @param percent The percent to get
	  * @param val the number to get it from
	  */
	public static double getPercent(double percent, double val){
		return (double)(val*(percent/100.0f));
	}
	

	/**
	 * Damages a Entity, returning the damage done
	  * @param e The Entity that shoudl be damaged
	  * @param d The damageing Player (may be null)
	  * @param damage The damage that should be dealt
	  * @param type The type of damage
	  * @param hurt Whether if the damaged entity should play the hurt animation
	  */
	public static double damage(LivingEntity e, Player d, double damage, DamageType type, boolean hurt){
		if(e instanceof Player){
			Player p = (Player)e;
			
		
			
			Kit k = KitManager.getKit(p);
			if(k == null) return 0;
			
			

			if(k.conditions.keySet().contains(Condition.PHYSICALIZE)) type = DamageType.PHYSICAL;
		
			damage = k.stats.calculate(type, damage);
			
			if(d != null){
				Kit k1 = KitManager.getKit(d);
				if(k1 != null){
					if(k1.conditions.containsKey(Condition.SUPER)) damage*=2; 
					if(k1.conditions.containsKey(Condition.TIRED)) damage/=2; 
					if(k1.conditions.containsKey(Condition.ENDGAME)) damage*=2; //part of Endgame code 
				}
				
				}
			
			if(k.conditions.keySet().contains(Condition.WEAK)) damage*=2;
			
			double dd = damage*100;
			
		damage = Math.round(dd)/100;
			
			
			
			k.damage(damage, hurt);
			
		}		else{
			e.damage(damage/10);
		}
		
		
		HoloAPI.getManager().createSimpleHologram(e.getEyeLocation().subtract(new Random().nextDouble()*2.0 - 1, new Random().nextDouble()*1.0, new Random().nextDouble()*2.0 - 1), 2, new Vector(0, 0.02, 0), type.getColor()+"-"+damage);
		
		return damage;
	}
	
	/**
	 * Closed the players current open inventory and opens a new when, avoiding conflicts
	  * @param p The player
	  * @param i the new inventory
	  */
	public static void open(final Player p, final Inventory i){
		if(p.getOpenInventory() != null){
		p.closeInventory();
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable(){

			@Override
			public void run() {
				p.openInventory(i);
			}
			
		}, 3);
	}
	
	/**
	 * Gives a condition to an Entity
	  * @param e The Entity
	  * @param c The condition to give
	  * @param duration The duration (NOTICE: this goes in 2 secs, so 1 = 2 SECONDS!!!!!)
	  */
	public static void giveCondtition(LivingEntity e, Condition c, int duration){
		if(e instanceof Player){
			Player p = (Player)e;
			
			Kit k = KitManager.getKit(p);
			if(k == null) return;
			
			k.addCondition(c, duration);
			
		}	
		
		HoloAPI.getManager().createSimpleHologram(e.getEyeLocation().subtract(0, new Random().nextDouble()*1.0, 0), 2, new Vector(0, 0.02, 0), c.getName());
	}
	
	/**
	 * Checks if an Entity shoudl be harmed by a player
	  * @param e The Entity
	  * @param p The Player
	  */
	public static boolean isInTeam(LivingEntity e, Player p){
		if(!(e instanceof Player)) return false;
		
		Player p2 = (Player)e;
		
		return Main.getInstance().km.getTeam(p2).getName().contentEquals((Main.getInstance().km.getTeam(p).getName()));

	}
	
	/**
	 * Returns a sphereof blocks
	  */
	public static List<Block> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY){
        List<Block> circleblocks = new ArrayList<Block>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
 
        for(int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for(int y = (sphere ? cy - radius : cy); y < (sphere ? cy + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if(dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY, z);
                        circleblocks.add(l.getBlock());
                    }
                }
            }
        }
 
        return circleblocks;
    }
	
	/**
	 * Strike false lightning
	  * @param loc The location where the ligthning should strike
	  * @param damage The damage it should deal
	  * @param range The range it should deal damage in
	  * @param p The damager
	  */
	public static void strikeFakeLightning(Location loc, int damage, int range, Player p){
		loc.getWorld().strikeLightningEffect(loc);
		
		for(LivingEntity e : loc.getWorld().getLivingEntities()){
			if(e.getLocation().distance(loc) <= range && !isInTeam(e, p)) damage(e, p,  damage, DamageType.LIGHTNING);
		}
		
	}

	/**
	 * Plays a Effect in a circle
	  * @param center The center Location
	  * @param radius The radius
	  * @param e The Effect to play
	  * @param id The Effect id
	  */
	public static void playEffectInCircle(final Location center, final int radius, final Effect e, final int id){
		Thread t = new Thread(){
			@Override
			public void run(){
				List<Block> circle = circle(center, radius, 1, false, false, 0);
				
				for(Block b : circle){
					b.getWorld().playEffect(b.getLocation(), e, id);
				}
			}
		};
		
		t.run();
	
	}
	
	/**
	 * Plays a ParticleEffect in a circle
	  * @param center The center Location
	  * @param radius The radius
	  * @param e The ParticleEffect to play
	  * @param amoutn The amount to play
	  */
	public static void playEffectInCircle(final Location center, final int radius, final ParticleEffect e, final int amount){
		Thread t = new Thread(){
			@Override
			public void run(){
				List<Block> circle = circle(center, radius, 1, false, false, 0);
				
				for(Block b : circle){
					Location l5 = b.getLocation().add(0, 0, .5);
					Location l6 = b.getLocation().add(0, 0, -.5);
					Location l7 = b.getLocation().add(.5, 0, 0);
					Location l8 = b.getLocation().add(-.5, 0, 0);
					

				
					
				e.animateAtLocation(l5, amount, 1);
				e.animateAtLocation(l6, amount, 1);
				e.animateAtLocation(l7, amount, 1);
				e.animateAtLocation(l8, amount, 1);
				}
			}
		};
		
		t.run();
	
	}
	
	public static ItemStack setSkullOwner(String owner, ItemStack skull){
		skull.setType(Material.SKULL_ITEM);
		skull.setDurability((byte) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(owner);
		skull.setItemMeta(meta);
		return skull;
	}
	
}
