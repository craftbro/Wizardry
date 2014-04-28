package code.wizard.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class NamedStack extends ItemStack{
	
	
	public NamedStack(String s, Material m){
		this(s, m, 1, (byte) 0);
	}
	
	public NamedStack(String s, Material m, int i){
		this(s, m, i, (byte) 0);
	}
	
	public NamedStack(String s, Material m, String[] lore){
		this(s, m, 1, (byte) 0);
		
		for(String l : lore){
			addLore(l);
		}
	}
	
	public NamedStack(String s, Material m, Color c, String[] lore){
		this(s, m, 1, (byte) 0);
		
		for(String l : lore){
			addLore(l);
		}
		
		color(c);
	}
	
	public NamedStack(String s, Material m, int i, byte b, String[] lore){
		this(s, m, i, b);
		
		for(String l : lore){
			addLore(l);
		}
	}
	
	public NamedStack(String s, Material m, int i,String[] lore){
		this(s, m, i);
		
		for(String l : lore){
			addLore(l);
		}
	}
	
	public NamedStack(String s, Material m, int i, byte b){
		super(m, i, b);
		
		name(s);
	}
	
	public void addLore(String s){
		ItemMeta meta = this.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if(meta.hasLore()){
			lore = meta.getLore();
		}
		lore.add(s);
		meta.setLore(lore);
		this.setItemMeta(meta);
	}
	
	private void name(String s){
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(s);
		this.setItemMeta(meta);
	}
	
	private void color(Color c){
		LeatherArmorMeta meta = (LeatherArmorMeta)this.getItemMeta();
		meta.setColor(c);
		this.setItemMeta(meta);
	}


	

}
