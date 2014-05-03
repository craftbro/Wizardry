package code.wizard.spell;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;

public class SpellHeatwave extends Spell{

	public SpellHeatwave(Player p) {
		super(p);
		
		name = ChatColor.RED+"Heatwave";
		stack = new ItemStack(Material.BLAZE_POWDER);
		cost = 0;
		
		des.add(ChatColor.DARK_AQUA+"Starts a very hot Wave of Heat");
		des.add(ChatColor.DARK_AQUA+"every emeny gains"+ChatColor.GRAY+" Weakend "+ChatColor.DARK_AQUA+" for "+ChatColor.WHITE+"26"+ChatColor.DARK_AQUA+" seconds");
		des.add(ChatColor.DARK_AQUA+"and "+ChatColor.RED+"Burns "+ChatColor.DARK_AQUA+"them for "+ChatColor.WHITE+"22"+ChatColor.DARK_AQUA+" seconds");

		rem.add(Condition.WEAK.getReminder());
		rem.add(Condition.BURN.getReminder());
		
		slot = SpellSlot.SPECIAL;
	}

	
	@Override
	public void cast(){
		cooldown = 32;
					for (Player p2 : Bukkit.getOnlinePlayers()){
						if (!Main.getInstance().lobby.spec.contains(p2)){
							if (!BasicUtil.isInTeam(p2, p)){
								BasicUtil.giveCondtition(p2, Condition.WEAK, 13);
								BasicUtil.giveCondtition(p2, Condition.BURN, 11);
							}
						}
					}
		new BukkitRunnable(){
			int times = 0;
			public void run(){
				for (Player p : Bukkit.getOnlinePlayers()){
					ParticleEffect effect = ParticleEffect.FLAME;
					effect.setStack(10, 7, 10);
					effect.animateToPlayer(p, 15, (float) new Random().nextDouble());
					effect = ParticleEffect.LAVA;
					effect.setStack(8, 5, 8);
					effect.animateToPlayer(p, 10, 1);
				}
				if (times >= 220){
					cancel();
				}
				times++;
			}
		}.runTaskTimer(Main.getInstance(), 2, 2);
	}
}
