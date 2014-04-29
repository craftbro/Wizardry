package code.wizard.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

import code.wizard.main.Main;
import code.wizard.main.Mode;
import code.wizard.special.smash;

public class KitManager implements Listener {

	private static HashMap<Player, Kit> kits = new HashMap<Player, Kit>();
	private static HashMap<Player, WizTeam> teams = new HashMap<>();
	
	Main plugin;
	
	
	static WizTeam team1;
	static WizTeam team2;
	
	List<Player> low = new ArrayList<Player>();
	
	boolean spawned = false;
	
	public KitManager(Main instance){
		plugin = instance;
		
	}


	
	public WizTeam getTeam(Player p){
		Kit kit = getKit(p);
		
		if(kit == null) return null;
		
		return kit.team;
	}
	
	public static void setupTeams(){
	
		team1 = new WizTeam(ChatColor.AQUA+"Eagles");
		team2 = new WizTeam(ChatColor.GREEN+"Seals");
		
	}
	
	public WizTeam putOnTeam(Player p){

		
		if(plugin.lobby.mode == Mode.TEAM){
			int l1 = team1.players.size();
			int l2 = team2.players.size();
			
		if(l2 > l1){
			team1.addPlayer(p);
			p.sendMessage(plugin.getPersonalPrefix()+"You're on Team "+team1.getName());
			return team1;
		}else{
			team2.addPlayer(p);
			p.sendMessage(plugin.getPersonalPrefix()+"You're on Team "+team2.getName());
			return team2;
		}
		}else{
			return new WizTeam(p.getName());
		}
		
		
	}
	
	public void tick(){
		for(Kit kit : kits.values()) kit.tick();
		
		if(plugin.lobby.started && !plugin.lobby.pperiod && !plugin.lobby.ended){
			if(plugin.lobby.mode == Mode.FFA){
			if(kits.size() == 1){
				for(Player p : kits.keySet()){
				plugin.lobby.end(p);
				}
			}
			}else{
				if(team1.players.size() == 0){
				plugin.lobby.end(team2);
				}else if(team2.players.size() == 0){
					plugin.lobby.end(team1);
				}
			}
		}
	}
	
    public void slowTick() {
    	for(Kit kit : kits.values()) kit.slowTick();
    	
    	if(!spawned && Lobby.started){
    	for(Player p  : kits.keySet()){
    		if(low.contains(p)) continue;
    		if(getKit(p).health <= 500) low.add(p);
    	}
    	
    	if(low.size() == kits.size() && !plugin.lobby.ended && !spawned){
    		new smash().spawn(plugin.lobby.map);
    		spawned = true;
    	}
    	}
	}
    
    public void secondTick() {
    	for(Kit kit : kits.values()) kit.secondTick();
	}
	
    public static void unregister(Player p){
    	kits.remove(p);
    }

    @EventHandler
    public void leave(PlayerQuitEvent event){
    	getKit(event.getPlayer()).team.players.remove(event.getPlayer());
    	kits.remove(event.getPlayer());
    }
    
    @EventHandler
    public void tag(PlayerReceiveNameTagEvent event){
    	Player p = event.getPlayer();
    	Player np = event.getNamedPlayer();
    	
    	WizTeam tp = getTeam(p);
    	WizTeam tnp = getTeam(np);
    	
    	if(tp == null || tnp == null) return;
    	
    	if(tp.getName().contentEquals(tnp.getName())){
    		event.setTag(ChatColor.GREEN+event.getTag());
    	}else{
    		event.setTag(ChatColor.RED+event.getTag());
    	}
    }
    
	@EventHandler
	public void slot(PlayerItemHeldEvent event){
		Player  p = event.getPlayer();
		
		if(!kits.containsKey(p)) return;
		
		
		Kit kit = kits.get(p);
		
		kit.manageSwitch(event);
		
		//event.setCancelled(true);
		
	}
	
	public void giveKit(Player p, WizTeam t){
		if(kits.containsKey(p)) return;
		
		Kit kit = new Kit(plugin, p, t);
		
		kit.gearUp();
		
		kits.put(p, kit);
	}


	public static Kit getKit(Player p) {
		
		return kits.containsKey(p) ? kits.get(p) : null;
	}


	
	
}
