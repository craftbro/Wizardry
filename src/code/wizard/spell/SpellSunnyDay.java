package code.wizard.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.player.KitManager;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;

public class SpellSunnyDay extends Spell{

	public SpellSunnyDay(Player p) {
		super(p);
		
		name = ChatColor.GREEN+"Draining Blow";
		stack = new ItemStack(Material.QUARTZ);
		cost = 75;
		
		des.add(ChatColor.DARK_AQUA+"Turns the sun very harsh damaging");
		des.add(ChatColor.WHITE+"5"+ChatColor.GOLD+" Light"+ChatColor.DARK_AQUA+" damage to every emeny");
		des.add(ChatColor.WHITE+"4"+ChatColor.DARK_AQUA+" times a second");

		info.put("Duration", ChatColor.GREEN+"14");
		
		slot = SpellSlot.SPECIAL;
	}

	
	@Override
	public void cast(){
		cooldown = 32;

		new BukkitRunnable(){
			int times = 0;
			public void run(){
				if (times >= 280){
					cancel();
				} else {
					for (int i=1;i<30;i++){
						ParticleEffect.RED_DUST.animateAtLocation(p.getEyeLocation().add(0, i / (1.8 - (times % 2 == 0 ? 0.6 : 0)), 0), 1, 0);
					}
					if (times % 4 == 0){
						if (times % 6 == 0){
							BasicUtil.damage(p, p, 5, DamageType.LIGHT);
						} else {
							BasicUtil.damage(p, p, 5, DamageType.LIGHT, false);
							p.playSound(p.getLocation(), Sound.HURT_FLESH, 0.9F, (new Random().nextInt(3) / 10 + 0.9F));
						}
					}
				}
				times++;
			}
		}.runTaskTimer(Main.getInstance(), 1, 1);
	}
}
