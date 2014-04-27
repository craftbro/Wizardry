package code.wizard.util;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import code.wizard.main.Main;


public class ProjectileWrapper implements Listener{

	
	private Projectile p;
	private ItemStack stack;
	private int duration = -1;
	private List<LivingEntity> hit;
	private boolean timed = false;
	
	private Runnable onExplode = new Runnable(){

		@Override
		public void run() {
			
		}
		
	};
	
	
	
	public ProjectileWrapper(Projectile p){
		this.p = p;
		
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent event){
		if(event.getEntity().getUniqueId() == p.getUniqueId()){
			onExplode.run();
		}
	}
	
	
	/**
     * Set's what should happen when the projectile hits
     * @param newR the new Runnable
     */
	public void setRunnable(Runnable newR){
		onExplode = newR;
	}
}
