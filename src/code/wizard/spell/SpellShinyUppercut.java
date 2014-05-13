package code.wizard.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;

public class SpellShinyUppercut extends Spell{

	public SpellShinyUppercut(Player p) {
		super(p);
		
		name = ChatColor.YELLOW+"Shining Uppercut";
		stack = new ItemStack(Material.GHAST_TEAR);
		cost = 45;
		
		unlockable = true;
		findable = true;
		
		des.add(ChatColor.DARK_AQUA+"Makes you fly up in the air");
		des.add(ChatColor.DARK_AQUA+"dealing"+ChatColor.WHITE+" 9 "+ChatColor.GOLD+"Light"+ChatColor.DARK_AQUA+" damage to everyone hit and");
		des.add(ChatColor.DARK_AQUA+"throws them up in the air with you");

		info.put(ChatColor.GREEN+"Note", "Damages up to 4 times, depends on when they got into the uppercut");
		
		slot = SpellSlot.PRIMARY_STICK;
	}
	
	@Override
	public void cast(){
		p.setVelocity(new Vector(0, 1, 0));
		new BukkitRunnable() {
			Location loc = p.getLocation().add(p.getLocation().getDirection().normalize().setY(0));
			Location belowLoc;
			double heightMod = 0.24;
			int times = 0;
			List<LivingEntity> safeList = new ArrayList<LivingEntity>();
			public void run() {
				for (LivingEntity e : BasicUtil.getInRadius(loc, 2)){
					if (e.getUniqueId() != p.getUniqueId()){
						e.setVelocity(e.getVelocity().setY((e.getVelocity().getY() / 2.4) + (e.isOnGround() ? 0.6 : 0.098)));
					}
					if (!BasicUtil.isInTeam(e, p) && !safeList.contains(e)){
						safeList.add(e);
						BasicUtil.damage(e, p, 9, DamageType.LIGHT);
					}
				}
				belowLoc = loc.clone();
				belowLoc.setY(belowLoc.getY() - heightMod * 4);
				loc.setY(loc.getY() + heightMod);
				ParticleEffect.CRIT.animateAtLocation(loc, 1, 0);
				ParticleEffect.FIREWORK_SPARK.animateAtLocation(belowLoc, 1, 0);
				if (times >= 20){
					p.setVelocity(new Vector(0, 0.000001, 0));
				} else {
					p.setVelocity(new Vector(0, p.getVelocity().getY() - 0.026, 0));
				}
				if (times == 20){
					safeList.clear();
				}
				if (times == 40){
					safeList.clear();
				}
				if (times == 60){
					safeList.clear();
				}
				if (times >= 75){
					cancel();
				}
				heightMod /= 1.04;
				times++;
			}
		}.runTaskTimer(Main.getInstance(), 1, 1);
	}
}
