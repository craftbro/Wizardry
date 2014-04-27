package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

import code.wizard.effect.ParticleEffect;
import code.wizard.player.KitManager;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;
import code.wizard.util.ProjectileWrapper;

public class SpellMagicalGem extends Spell{

	public SpellMagicalGem(Player p) {
		super(p);
		
		name = ChatColor.GREEN+"Magical Gem";
		stack = new ItemStack(Material.EMERALD);
		cost = 75;
		
		des.add(ChatColor.DARK_AQUA+"Fires a magical gem that, on impact, deals 80"+ChatColor.GREEN+" Ground");
		des.add(ChatColor.DARK_AQUA+"damage to enemies and restores you 30 mana");
		
		info.put("Range", ChatColor.GREEN+"3");
		
		slot = SpellSlot.PRIMARY_WAND;
	}
	
	@Override
	public void cast(){
final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.EMERALD));
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 3)){
					if(!BasicUtil.isInTeam(e, p)){ BasicUtil.damage(e, p, 80, DamageType.GROUND); KitManager.getKit(p).giveMana(30);}
				}
				
				item.getWorld().playSound(item.getLocation(), Sound.ANVIL_BREAK, 5, 2);
				
				BasicUtil.playEffectInCircle(item.getLocation().subtract(0, 1, 0), 3, Effect.STEP_SOUND, Material.EMERALD_BLOCK.getId());
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.DIG_STONE, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(1.6));
		
	}
	
	

}
