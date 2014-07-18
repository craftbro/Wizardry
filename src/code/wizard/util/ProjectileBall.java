package code.wizard.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.main.Main;


public class ProjectileBall{

	
	private Item item;
	private ItemStack stack;
	private int duration = -1;
	private List<LivingEntity> hit;
	private boolean timed = false;
	private boolean dead = false;
	
	private Runnable onExplode = new Runnable(){

		@Override
		public void run() {
			
		}
		
	};
	
	@SuppressWarnings("deprecation")
	/**
     * Fires the item
     * @param l The start location for the item
     * @param v The vector the item is fired with
     */
	public void launch(final Location l, Vector v){
		dead = false;
		stack.setAmount(1);
		
		item = l.getWorld().dropItem(l, stack);
		item.setPickupDelay(Integer.MAX_VALUE);
		item.setVelocity(v);
		
		
		if(timed){
			new BukkitRunnable(){

				int i = 0;
				
				@Override
				public void run() {
					if(i >= duration){ onExplode.run(); cancel();}else{i++;}
					
				}
				
			}.runTaskTimer(Main.getInstance(), 0, 1);
		}else{
			new BukkitRunnable(){

				@Override
				public void run() {
					if (dead){
						cancel();
						return;
					}
					BlockFace[] faces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH};
					
					for(BlockFace f : faces){
						Block b = item.getLocation().getBlock().getRelative(f);
						
						if(b.getType().isSolid() && !dead){
							onExplode.run();
							cancel();
							dead = true;
							break;
						}
					}
				}
				
			}.runTaskTimer(Main.getInstance(), 5, 1);
		}
		
		
	}
	
	/**
     * Sets up a new Projectileball
     * @param i The item
     * @param d The duration after the item should explode
     */
	public ProjectileBall(ItemStack i, int d){
		stack = i;
		duration = d;
		timed = true;
		//Main.print("delayed");
	}
	
	/**
     * Sets up a new Projectileball (this makes the item explode on impact)
     * @param i The item
     */
	public ProjectileBall(ItemStack i){
		stack = i;
	}
	
	/**
     * Returns the item
     */
	public Item getItem(){
		return item;
	}
	
	/**
     * Set's what should happen when the projectile hits
     * @param newR the new Runnable
     */
	public void setRunnable(Runnable newR){
		onExplode = newR;
	}
}
