package code.wizard.lobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import code.wizard.main.Main;
import code.wizard.main.Mode;

public class Lobby  implements Listener {

	Main plugin;
	
	Location lobby;
	
	public int cc = 60;	
	public int req = 2;
	
	public Mode mode;
	
	public List<Player> spec = new ArrayList<Player>();
	

	public boolean ended = false;
	
	public static boolean started = false;
	
	public int peace = 15;
	public boolean pperiod = false;
	
	public Lobby(Main instance) {
		plugin = instance;

		setup();
	}

	protected void setup(){};
	
	public void tick(){};
	
	public Location getSpawn(Player p){
		return null;
	}
	
	public Location getSmashSpawn(){
		return null;
	}
	
	public static boolean hasStarted() {
		return started;
	}
	
}
