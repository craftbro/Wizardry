package code.wizard.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.server.v1_7_R3.MobEffect;
import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
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
	
	protected static HashMap<Player, Player> killers = new HashMap<Player, Player>();

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
	public static double damage(LivingEntity e, Player d, double damage, DamageType type){
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
			
			double multiply = 1;
			double divide = 1;

			if(k.conditions.keySet().contains(Condition.PHYSICALIZE)) type = DamageType.PHYSICAL;
		
			damage = k.stats.calculate(type, damage);
			
			if(d != null){
				Kit k1 = KitManager.getKit(d);
				if(k1 != null){
					if(k1.conditions.containsKey(Condition.SUPER)) multiply+=1; 
					if(k1.conditions.containsKey(Condition.TIRED)) divide+=1; 
					if(k1.conditions.containsKey(Condition.ENDGAME)) multiply+=1; //part of Endgame code 
				}
				
				}
			
			if(k.conditions.keySet().contains(Condition.WEAK)) multiply+=1;
			
			damage *= multiply;
			damage /= divide;
			
			if ((damage - 10 < 1 && damage >= 1) == false){
				if(k.conditions.keySet().contains(Condition.HARDEN)) damage-=10;
			}
			
			double dd = damage*100;
			
		damage = Math.round(dd)/100;
			
			if(d != null) killers.put(p,d);
			
			k.damage(damage, hurt);
			
			if(k.health <= 0){
				if(killers.containsKey(p)){
					Main.getInstance().sql.handler.addScore(killers.get(p), 1);
					killers.get(p).sendMessage(Main.getPersonalPrefix()+ChatColor.GREEN+"+1 Score for killing "+p.getName());
				}
			}
			
		}		else{
			
			double dd = damage*100;
			
		damage = Math.round(dd)/100;
		
			e.damage(damage/10);
		}
		
		
		HoloAPI.getManager().createSimpleHologram(e.getEyeLocation().subtract(new Random().nextDouble()*2.0 - 1, new Random().nextDouble()*1.0, new Random().nextDouble()*2.0 - 1), 2, new Vector(0, 0.02, 0), type.getColor()+"-"+(int)damage);
		
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
	
	/**
	 * Turns an item into a skull with specific owner (probaly broken)
	 * @param owner the username of the head
	 * @param skull the item to give skull owner (gets turned into skull)
	 * @return the same item transformed to the skull
	 */
	public static ItemStack setSkullOwner(String owner, ItemStack skull){
		skull.setType(Material.SKULL_ITEM);
		skull.setDurability((byte) 3);
		SkullMeta meta = (SkullMeta) skull.getItemMeta();
		meta.setOwner(owner);
		skull.setItemMeta(meta);
		return skull;
	}
	
	/**
	 * Creates a fake explosion that damages
	 * @param loc the location for the explosion
	 * @param p the player that caused the explosion (may be null)
	 * @param radius the radius of the explosion
	 * @param damage the amount of damage
	 * @param type the type of damage
	 * @param fireParticles if the explosion should use fire particles too
	 * @param damageDecrease if damage should decrese the father away the hit entity is
	 * @param c the condition to give
	 * @param duration the duration of the condition (1 = 2 seconds!!!)
	 * @param damageDecrease if the explosion can damage teammates
	 * @return a hashmap containing information (key: Living Entity hurt, Damage done)
	 */
	public static HashMap<LivingEntity, Double> spawnFalseExplosion(Location loc, Player p, float radius, double damage, DamageType type, boolean fireParticles, boolean damageDecrease, Condition c, int duration, boolean friendlyFire){
		HashMap<LivingEntity, Double> damaged = new HashMap<LivingEntity, Double>();
		for (Player p2 : Bukkit.getOnlinePlayers()){
			if (p2.getLocation().distance(loc) <= radius * 50){
				((CraftPlayer) p2).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles("largeexplode", (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), radius/2, radius/2, radius/2, 0.1F, Math.round(radius * 4)));
				((CraftPlayer) p2).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles("largesmoke", (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), radius/2+0.1F, radius/2+0.1F, radius/2+0.1F, 0, Math.round(radius * 5)));
				if (fireParticles){
					((CraftPlayer) p2).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles("flame", (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), radius/2+0.1F, radius/2+0.1F, radius/2+0.1F, 0, Math.round(radius * 5)));
				}
				((CraftPlayer) p2).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles("explode", (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), radius/2+0.1F, 0, radius/2+0.1F, 0.2F, Math.round(radius * 6)));
			}
		}
		loc.getWorld().playSound(loc, Sound.EXPLODE, 1.6F, 0);
		for (LivingEntity e : getInRadius(loc, radius)){
			if (!friendlyFire){
				if (!isInTeam(e, p)){
					if (damageDecrease){
						double decreasedDamage = damage / e.getLocation().distance(loc);
						if (decreasedDamage > damage){
							decreasedDamage = damage;
						}
						damaged.put(e, damage(e, p, decreasedDamage, type, true));
					} else {
						damaged.put(e, damage(e, p, damage, type));
					}
					if (c != null && duration != 0){
						giveCondtition(e, c, duration);
					}
				}
			} else {
				if (damageDecrease){
					double decreasedDamage = damage / e.getLocation().distance(loc);
					if (decreasedDamage > damage){
						decreasedDamage = damage;
					}
					damaged.put(e, damage(e, p, decreasedDamage, type, true));
				} else {
					damaged.put(e, damage(e, p, damage, type));
				}
				if (c != null && duration != 0){
					giveCondtition(e, c, duration);
				}
			}
		}
		return damaged;
	}
	
	/**
	 * Creates a fake explosion that damages
	 * @param loc the location for the explosion
	 * @param p the player that caused the explosion (may be null)
	 * @param radius the radius of the explosion
	 * @param damage the amount of damage
	 * @param type the type of damage
	 * @param fireParticles if the explosion should use fire particles too
	 * @param damageDecrease if damage should decrese the father away the hit entity is
	 * @param damageDecrease if the explosion can damage teammates
	 * @return a hashmap containing information (key: Living Entity hurt, Damage done)
	 */
	public static HashMap<LivingEntity, Double> spawnFalseExplosion(Location loc, Player p, float radius, double damage, DamageType type, boolean fireParticles, boolean damageDecrease, boolean friendlyFire){
		return spawnFalseExplosion(loc, p, radius, damage, type, fireParticles, damageDecrease, null, 0, friendlyFire);
	}
	
	/**
	 * Creates a fake explosion that damages
	 * @param loc the location for the explosion
	 * @param p the player that caused the explosion (may be null)
	 * @param radius the radius of the explosion
	 * @param damage the amount of damage
	 * @param type the type of damage
	 * @param damageDecrease if damage should decrese the father away the hit entity is
	 * @param damageDecrease if the explosion can damage teammates
	 * @return a hashmap containing information (key: Living Entity hurt, Damage done)
	 */
	public static HashMap<LivingEntity, Double> spawnFalseExplosion(Location loc, Player p, float radius, double damage, DamageType type, boolean damageDecrease, boolean friendlyFire){
		return spawnFalseExplosion(loc, p, radius, damage, type, false, damageDecrease, friendlyFire);
	}
}
