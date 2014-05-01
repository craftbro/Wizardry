package code.wizard.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;

public class SpellFireBlast extends Spell{
	public static List<String> list = new ArrayList<String>();
	
	public SpellFireBlast(Player p) {
		super(p);
		
		name = ChatColor.DARK_RED+"Fire Blast";
		stack = new ItemStack(Material.FIRE);
		cost = 100;
		
		unlockable = true;
		findable = true;
		
		des.add(ChatColor.DARK_AQUA+"First time used collect power to unleash the spell");
		des.add(ChatColor.DARK_AQUA+"next time you throw a huge Fireblast dealing");
		des.add(ChatColor.WHITE+"90"+ChatColor.RED+" Fire"+ChatColor.DARK_AQUA+" damage to everyone it hits");
		des.add(ChatColor.DARK_AQUA+"and "+ChatColor.RED+"Burns"+ChatColor.DARK_AQUA+" them for "+ChatColor.WHITE+"12"+ChatColor.DARK_AQUA+" seconds");

		info.put("Range", ChatColor.GREEN+"5");
		info.put(ChatColor.RED+"Note:", "Charging up makes you weak for 8 seconds");
		
		rem.add(Condition.BURN.getReminder());
		rem.add(Condition.WEAK.getReminder());
		
		slot = SpellSlot.PRIMARY_WAND;
	}

	
	@Override
	public void cast(){
		Builder b = FireworkEffect.builder().withColor(Color.ORANGE).withColor(Color.YELLOW).withFade(Color.RED);
		FireworkEffect effectS = b.with(Type.BALL).build();
		final FireworkEffect effectB = b.with(Type.BALL_LARGE).build();
		
		if (list.contains(p.getName())){
			list.remove(p.getName());
			new BukkitRunnable(){
				public Location currLoc = p.getEyeLocation();
				public Vector vec = currLoc.getDirection().normalize().multiply(0.7);
				int times = 0;
				BlockFace[] faces = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH};
				public void run(){
					currLoc.add(vec);
					if (times >= 400){
						cancel();
						try{
							CodeEffect.playFirework(currLoc, effectB);
						}catch(Exception e1){
							e1.printStackTrace();
						}
						for (LivingEntity e : BasicUtil.getInRadius(currLoc, 5)){
							if (!BasicUtil.isInTeam(e, p)){
								BasicUtil.damage(e, p, 90, DamageType.FIRE);
								BasicUtil.giveCondtition(e, Condition.BURN, 6);
							}
						}
					} else {
						
						for(BlockFace f : faces){
							Block b = currLoc.getBlock().getRelative(f);
							
							if(b.getType().isSolid()){
								cancel();
								try{
									CodeEffect.playFirework(currLoc, effectB);
								}catch(Exception e1){
									e1.printStackTrace();
								}
								for (LivingEntity e : BasicUtil.getInRadius(currLoc, 5)){
									if (!BasicUtil.isInTeam(e, p)){
										BasicUtil.damage(e, p, 90, DamageType.FIRE);
										BasicUtil.giveCondtition(e, Condition.BURN, 6);
									}
								}
								break;
							}
						}
					}
					ParticleEffect.FLAME.animateAtLocation(currLoc, 5, 1);
					ParticleEffect.LAVA.animateAtLocation(currLoc, 10, 1);
					times++;
				}
			}.runTaskTimer(Main.getInstance(), 1, 1);
		} else {
			list.add(p.getName());
			BasicUtil.giveCondtition(p, Condition.WEAK, 4);
			try{
				CodeEffect.playFirework(p.getEyeLocation().add(0, 0.75, 0), effectS);
			}catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
}
