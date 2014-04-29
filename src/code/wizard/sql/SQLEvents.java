package code.wizard.sql;

import java.sql.Connection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import code.wizard.main.Main;
import code.wizard.spell.SpellCharCharge;

public class SQLEvents implements Listener {

	
	Main plugin;

	public SQLEvents(Main instance){
		plugin = instance;
	}
	
	
	@EventHandler
	public void join(PlayerJoinEvent event){
		Player p = event.getPlayer();
		
		
			plugin.sql.addPlayer(p);
	
		
		
		int logins = (int) plugin.sql.getData(p, "logins");
		
		logins++;
		
		plugin.sql.alterData(p, "logins", logins);
		
		if(logins >= 3){
			plugin.find.giveSpell(p, "logging in three times", new SpellCharCharge(null));
		}
		
	}
	
	
	
}
