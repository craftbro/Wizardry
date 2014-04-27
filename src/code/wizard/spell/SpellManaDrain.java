package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import code.wizard.effect.ParticleEffect;
import code.wizard.player.KitManager;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.ProjectileBall;

public class SpellManaDrain extends Spell{

	public SpellManaDrain(Player p) {
		super(p);
		
		name = ChatColor.YELLOW+"Mana Sponge";
		stack = new ItemStack(Material.SPONGE);
		cost = 30;
		
		des.add(ChatColor.DARK_AQUA+"Fires a sponge that sucks ");
		des.add(ChatColor.DARK_AQUA+"up "+ChatColor.WHITE+"25"+ChatColor.DARK_AQUA+" mana of nearby enemies");
		
		info.put("Range", ChatColor.GREEN+"5");
		
		
		slot = SpellSlot.PRIMARY_MELEE;
	}
	
	@Override
	public void cast(){
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.SPONGE));
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 5)){
					if(e instanceof Player && !BasicUtil.isInTeam(e, p))KitManager.getKit((Player)e).takeMana(25);
				}
				
			
				
				BasicUtil.playEffectInCircle(item.getLocation(), 5, Effect.STEP_SOUND, Material.SPONGE.getId());
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.SHOOT_ARROW, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(1.6));
		
	}
	
	

}
