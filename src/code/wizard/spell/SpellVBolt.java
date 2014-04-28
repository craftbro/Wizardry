package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.dsh105.holoapi.HoloAPI;
import com.dsh105.holoapi.api.Hologram;

import code.wizard.effect.CodeEffect;
import code.wizard.main.Main;

public class SpellVBolt extends Spell{

	public SpellVBolt(Player p) {
		super(p);
		
		name = ChatColor.AQUA+"Virtual Bolt";
		stack = new ItemStack(Material.DIAMOND_BLOCK);
		cost = 40;
		
		des.add(ChatColor.DARK_AQUA+"Shoots a virtual bolt that");
		des.add(ChatColor.DARK_AQUA+"deals "+ChatColor.WHITE+"70-120"+ChatColor.GREEN+" Ground"+ChatColor.DARK_AQUA+" damage on impact");
		des.add(ChatColor.DARK_AQUA+"and damage nearby emenies "+ChatColor.WHITE+"20-30"+ChatColor.GREEN+" Ground damage");

		info.put("Primary Range", "1");
		info.put("Nearby Range", "4");
		
		slot = SpellSlot.PRIMARY_WAND;
	}
	
	@Override
	public void cast(){

		new BukkitRunnable(){
			Hologram bolt = HoloAPI.getManager().createSimpleHologram(p.getLocation(), 300, p.getLocation().getDirection().normalize().multiply(0.6), new String[]{
				ChatColor.AQUA+"■"
			});
			public void run(){
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	
		p.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 5, 1);
	
		FireworkEffect effect = FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.fromBGR(225, 225, 255)).with(Type.BURST).build();
	
		try {
			CodeEffect.playFirework(p.getWorld(), p.getLocation(), effect);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	

}
