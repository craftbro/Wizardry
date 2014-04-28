package code.wizard.player;

import org.bukkit.entity.Player;

import code.configtesting.config.Config;
import code.wizard.spell.Spell;
import code.wizard.spell.SpellLoader;

public class PlayerDataLoader {

	Player p;
	
	String pm;
	String sm;
	String ps;
	String ss;
	String pw;
	String sw;
	
	
	public PlayerDataLoader(Player p){
		this.p = p;
	}
	
	public void loadData(){
		register();
		
		pm = (String) Config.getData(p, "pm");
		sm = (String) Config.getData(p, "sm");
		ps = (String) Config.getData(p, "ps");
		ss = (String) Config.getData(p, "ss");
		pw = (String) Config.getData(p, "pw");
		sw = (String) Config.getData(p, "sw");
	}
	
	public Spell getPM(){
		return SpellLoader.loadSpell(p, pm);
	}
	
	public Spell getSM(){
		return SpellLoader.loadSpell(p, sm);
	}
	
	public Spell getPS(){
		return SpellLoader.loadSpell(p, ps);
	}
	
	public Spell getSS(){
		return SpellLoader.loadSpell(p, ss);
	}
	
	public Spell getPW(){
		return SpellLoader.loadSpell(p, pw);
	}
	
	public Spell getSW(){
		return SpellLoader.loadSpell(p, sw);
	}
	
	private void register(){
		if(Config.getData(p, "pm") == null){
			Config.setData(p, "pm", "Coal Bomb");
			Config.setData(p, "sm", "Uppercut");
			Config.setData(p, "ps", "Fire Bomb");
			Config.setData(p, "ss", "Flash Bomb");
			Config.setData(p, "pw", "Thunder Rain");
			Config.setData(p, "sw", "Holy Fountain");
		}
	}
	
	
}
