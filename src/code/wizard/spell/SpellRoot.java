package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;

public class SpellRoot extends Spell{

	public SpellRoot(Player p) {
		super(p);
		
		name = ChatColor.GREEN+"Roots";
		stack = new ItemStack(Material.LOG);
		cost = 55;
		
		des.add(ChatColor.DARK_AQUA+"Smashes roots up from the ground");
		des.add(ChatColor.DARK_AQUA+"Dealing "+ChatColor.WHITE+"45"+ChatColor.GREEN+" Ground"+ChatColor.DARK_AQUA+" damage");
		des.add(ChatColor.DARK_AQUA+"And "+ChatColor.DARK_PURPLE+"Slowing"+ChatColor.DARK_AQUA+" them for "+ChatColor.WHITE+"6"+ChatColor.DARK_AQUA+" Seconds");

		info.put("Range", ChatColor.GREEN+"15");
		
		rem.add(Condition.SLOW.getReminder());
		
		slot = SpellSlot.PRIMARY_STICK;
	}
	
	@Override
	public void use(){
		float xp = p.getExp();
		float xpCost = cost/100;
		
		if(xp >= xpCost &&  p.getTargetBlock(null, 15).getType() != Material.AIR){
			p.setExp(xp - xpCost);
			cast();
		}
	}
	
	@Override
	public void cast(){
		final Block block = p.getTargetBlock(null, 15);
		
		ParticleEffect particle = ParticleEffect.TILECRACK;
		particle.setId(Material.LEAVES.getId());
		particle.animateAtLocation(block.getLocation().add(0, 0.6, 0), 10, 1);
		particle.setId(1);
		p.getWorld().playSound(p.getLocation(), Sound.ZOMBIE_WOOD, 0.6F, 1.3F);
		new BukkitRunnable(){
			public void run(){
				p.getWorld().playSound(p.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.6F, 1.3F);
				for (LivingEntity e : BasicUtil.getInRadius(block.getLocation().add(0, 1, 0), 3)){
					if (!BasicUtil.isInTeam(e, p)){
						BasicUtil.damage(e, p, 45, DamageType.GROUND);
						BasicUtil.giveCondtition(e, Condition.SLOW, 3);
						
						ParticleEffect particle = ParticleEffect.HAPPY_VILLAGER;
						particle.setStack(1, 2, 1);
						particle.animateAtLocation(e.getLocation(), 10, 1);
						particle.setStack(0, 0, 0);
					}
				}
			}
		}.runTaskLater(Main.getInstance(), (long) ((block.getLocation().distance(block.getLocation()) + 1) * 5));
	
		FireworkEffect effect = FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.fromBGR(225, 225, 255)).with(Type.BURST).build();
	
		try {
			CodeEffect.playFirework(p.getWorld(), p.getLocation(), effect);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	

}
