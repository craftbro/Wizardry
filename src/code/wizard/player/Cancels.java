package code.wizard.player;


import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;

public class Cancels implements Listener {

	
	
	
	
	@EventHandler
	public void pickup(PlayerPickupItemEvent e){
		 Player p = e.getPlayer();
		
		 if(p.getGameMode() != GameMode.CREATIVE)  e.setCancelled(true);
	}
	
	
	
	@EventHandler
	public void click(BlockBreakEvent event){
		Player p = event.getPlayer();
		
		if(p.getGameMode() == GameMode.CREATIVE) return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void fall(EntityDamageEvent event){
		if(event.getCause() == DamageCause.FALL || !Lobby.hasStarted()) event.setCancelled(true);
	}
	
	@EventHandler
	public void fall(EntityDamageByEntityEvent event){
		if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			if(Lobby.hasStarted() && !Main.getInstance().lobby.pperiod){
			
			Player pd = (Player)event.getDamager();
			Player pe = (Player)event.getEntity();
			
			if(pd.getItemInHand().getType().name().contains("SWORD")){
				Kit kit = KitManager.getKit(pe);
				Kit kit1 = KitManager.getKit(pd);
				
				if(kit != null && kit1 != null ){
					if(pd.getExp()/100 >= 5){
					BasicUtil.damage(pe, pd, 5, DamageType.PHYSICAL, false);
					event.setDamage(0);
					kit1.takeMana(5);
					}
				}else{
					event.setCancelled(true);
				}
			}else{
				event.setCancelled(true);
			}
			
			}else{
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=false)
	public void onDropItem(PlayerDropItemEvent e) {

Player player = e.getPlayer();

 Kit kit = KitManager.getKit(player);
    
 if(player.getGameMode() != GameMode.CREATIVE){
	e.getPlayer().setItemInHand(e.getItemDrop().getItemStack());
	e.getItemDrop().remove();
 if(kit != null)kit.onE();         
 }
	}
	
}
