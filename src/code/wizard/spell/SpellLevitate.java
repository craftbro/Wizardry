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

import code.wizard.effect.CodeEffect;
import code.wizard.main.Main;

public class SpellLevitate extends Spell{

	public SpellLevitate(Player p) {
		super(p);
		
		name = ChatColor.LIGHT_PURPLE+"Levitate";
		stack = new ItemStack(Material.INK_SACK, 1, (short) 9);
		cost = 40;
		
		des.add(ChatColor.DARK_AQUA+"Makes you float slowly up in the air");

		info.put(ChatColor.RED+"Note", "Cannot be uses while in the air");
		
		slot = SpellSlot.SECUNDAIRY_MELEE;
	}
	
	@Override
	public void use(){
		float xp = p.getExp();
		float xpCost = cost/100;
		
		if(xp >= xpCost && p.isOnGround()){
			p.setExp(xp - xpCost);
			cast();
		}
	}
	
	@Override
	public void cast(){

		new BukkitRunnable(){
			int timer = 0;
			double height = 1;
			double side = 0.4;
			public void run(){
				p.setVelocity(p.getLocation().getDirection().normalize().multiply(side).setY(height));
				height /= 1.18;
				side /= 1.0472;
				if (timer >= 35){
					cancel();
				}
				timer++;
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
	
		p.getWorld().playSound(p.getLocation(), Sound.ZOMBIE_INFECT, 5, 0);
	
		FireworkEffect effect = FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.fromBGR(225, 225, 255)).with(Type.BURST).build();
	
		try {
			CodeEffect.playFirework(p.getWorld(), p.getLocation(), effect);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	

}