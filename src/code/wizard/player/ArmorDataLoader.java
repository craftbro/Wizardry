package code.wizard.player;

import org.bukkit.entity.Player;

import code.configtesting.config.Config;
import code.wizard.armor.Armor;
import code.wizard.spell.Spell;
import code.wizard.spell.SpellLoader;

public class ArmorDataLoader {

	Player p;
	
	String hat;
	String cape;
	String pants;
	String boots;
	
	public ArmorDataLoader(Player p){
		this.p = p;
	}
	
	public void loadData(){
		register();
		
		hat = (String) Config.getData(p, "hat");
		cape = (String) Config.getData(p, "cape");
		pants = (String) Config.getData(p, "pants");
		boots = (String) Config.getData(p, "boots");
	}
	
	public Armor.hat getHat(){
		return Armor.hat.valueOf(hat);
	}
	
	public Armor.cape getCape(){
		return Armor.cape.valueOf(cape);
	}
	
	public Armor.pants getPants(){
		return Armor.pants.valueOf(pants);
	}
	
	public Armor.boots getBoots(){
		if(Armor.boots.valueOf(boots) == null) return Armor.boots.RUNNER_SHOES;
		return Armor.boots.valueOf(boots);
	}

	private void register(){
		if(Config.getData(p, "hat") == null){
			Config.setData(p, "hat", "TIN_HAT");	
			Config.setData(p, "cape", "MAGIC_CAPE");	
			Config.setData(p, "pants", "AQUA_TROUSERS");	
			Config.setData(p, "boots", "RUNNER_SHOES");	
		}
		if(Config.getData(p, "boots") == "RUNNER_BOOTS") Config.setData(p, "boots", "RUNNER_SHOES");
	}
	
	
}
