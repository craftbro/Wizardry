package code.wizard.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class WizTeam {

	String name;
	
	public List<Player> players = new ArrayList<>();
	
	public WizTeam(String name){
		this.name = name;
	}
	
	public void addPlayer(Player p){
		players.add(p);
	}
	
	public String getName(){
		return name;
	}
	
}
