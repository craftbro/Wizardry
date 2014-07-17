package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.effect.CodeEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;

public class SpellRocket extends Spell{

	public SpellRocket(Player p) {
		super(p);
		
		name = ChatColor.GOLD+"Rocket";
		stack = new ItemStack(Material.IRON_INGOT);
		cost = 40;
		
		des.add(ChatColor.YELLOW+"Stuns"+ChatColor.DARK_AQUA+" you for "+ChatColor.WHITE+"6"+ChatColor.DARK_AQUA+" seconds");
		des.add(ChatColor.DARK_AQUA+"When the Duration of the "+ChatColor.YELLOW+"Stun"+ChatColor.DARK_AQUA+" ends you will fly forward");
		des.add(ChatColor.DARK_AQUA+"While damaging everyone near you and "+ChatColor.DARK_RED+"Oiling"+ChatColor.DARK_AQUA+"them for "+ChatColor.WHITE+"10"+ChatColor.DARK_AQUA+" seconds");
		
		info.put(ChatColor.RED+"Note", "You wont move if your in the air when the stun ends");
		info.put("Damage", "10 Fire Damage");
		
		rem.add(Condition.STUN.getReminder());
		rem.add(Condition.OIL.getReminder());
		
		slot = SpellSlot.SECUNDAIRY_MELEE;
		
		unlockable = true;
		findable = true;
	}
	
	@Override
	public void use(){
		float xp = p.getExp();
		float xpCost = cost/100;
		
		if(xp >= xpCost){
			p.setExp(xp - xpCost);
			cast();
		}
	}
	
	@Override
	public void cast(){
	
		BasicUtil.giveCondtition(getPlayer(), Condition.STUN, 3);
		
	new BukkitRunnable(){
		public void run(){
			if (p.isOnGround()){
				p.setVelocity(new Vector(0, 1.127, 0));
				
				p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, 5, 0);
				p.getWorld().playSound(p.getLocation(), Sound.FIRE, 5, 0.6F);
				new BukkitRunnable() {
					
					@Override
					public void run() {
						p.setVelocity(p.getLocation().getDirection().normalize().multiply(0.9));
					}
				}.runTaskLater(Main.getInstance(), 15);
			}
			for (LivingEntity e : BasicUtil.getInRadius(p.getLocation(), 4)){
				if (!BasicUtil.isInTeam(e, p)){
					BasicUtil.damage(e, p, 10, DamageType.FIRE);
					BasicUtil.giveCondtition(e, Condition.OIL, 5);
				}
			}
			
			FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).withFade(Color.ORANGE).with(Type.BURST).build();
			try {
				CodeEffect.playFirework(p.getWorld(), p.getLocation(), effect);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}.runTaskLater(Main.getInstance(), 120);
		
	}
	
	

}
