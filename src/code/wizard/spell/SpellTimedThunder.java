package code.wizard.spell;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.server.v1_7_R3.PacketPlayOutWorldParticles;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import code.wizard.effect.ParticleEffect;
import code.wizard.main.Main;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;

public class SpellTimedThunder extends Spell{
	HashMap<String, Location> map = new HashMap<String, Location>();

	public SpellTimedThunder(Player p) {
		super(p);
		
		name = ChatColor.YELLOW+"Timed Thunder";
		stack = new ItemStack(Material.BLAZE_POWDER);
		cost = 60;
		
		unlockable = true;
		findable = true;
		
		des.add(ChatColor.DARK_AQUA+"Shoots out a thunder bomb");
		des.add(ChatColor.DARK_AQUA+"after"+ChatColor.WHITE+" 0.5 "+ChatColor.DARK_AQUA+" seconds it vanishes");
		des.add(ChatColor.DARK_AQUA+"use it again to blow it up for "+ChatColor.WHITE+"0"+ChatColor.DARK_AQUA+" mana dealing");
		des.add(ChatColor.WHITE+"60"+ChatColor.YELLOW+" Lightning "+ChatColor.DARK_AQUA+"damage and "+ChatColor.YELLOW+"Stuns"+ChatColor.DARK_AQUA+" them for "+ChatColor.WHITE+"20"+ChatColor.DARK_AQUA+" seconds");

		info.put("Range", ChatColor.GREEN+"4");
		
		rem.add(Condition.STUN.getReminder());
		
		slot = SpellSlot.PRIMARY_STICK;
	}

	public void use(){
		float xp = p.getExp();
		float xpCost = cost/100;
		
		if (map.containsKey(p.getName())){
			if (map.get(p.getName()) != null){
				for (LivingEntity e : BasicUtil.getInRadius(map.get(p.getName()), 4)){
					if (!BasicUtil.isInTeam(e, p)){
						BasicUtil.damage(e, p, 60, DamageType.LIGHTNING);
						BasicUtil.giveCondtition(e, Condition.STUN, 10);
					}
				}
				ParticleEffect particle = ParticleEffect.ANGRY_VILLAGER;
				particle.setStack(1, 1, 1);
				particle.animateAtLocation(map.get(p.getName()), 25, 1);
				map.remove(p.getName());
			}
		} else if(xp >= xpCost){
			if(cooldown > 0){ p.sendMessage(Main.getPersonalPrefix()+"This spell is still cooling down");return;}
			
			p.setExp(xp - xpCost);
				
			cast();
		}
	}
	
	@Override
	public void cast(){
		cooldown = 5;
		map.put(p.getName(), null);
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.BLAZE_POWDER), 10);
		ball.setRunnable(new Runnable(){
			public void run() {
				map.put(p.getName(), ball.getItem().getLocation());
				ball.getItem().remove();
				new BukkitRunnable(){
					public void run(){
						if (map.containsKey(p.getName())){
							if (map.get(p.getName()) != null){
								((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutWorldParticles("angryVillager", (float) map.get(p.getName()).getX(), (float) map.get(p.getName()).getY(), (float) map.get(p.getName()).getZ(), 0, 0, 0, 1, 1));
								return;
							}
						}
					}
				}.runTaskTimer(Main.getInstance(), 1, 10);
			}
		});
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(1.5));
	}
}
