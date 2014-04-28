package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

import code.wizard.effect.CodeEffect;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;
import code.wizard.util.ProjectileWrapper;

public class SpellCoalBomb extends Spell{

	public SpellCoalBomb(Player p) {
		super(p);
		
		name = ChatColor.DARK_GRAY+"Coal Bomb";
		stack = new ItemStack(Material.COAL);
		cost = 35;
		
		des.add(ChatColor.DARK_AQUA+"Fires a bomb that on impact, deals");
		des.add(ChatColor.WHITE+"25 "+ChatColor.DARK_GRAY+"Dark "+ChatColor.DARK_AQUA+"damage to enemies");
		
		
		info.put("Range", ChatColor.GREEN+"4");
	
		
	
		
		slot = SpellSlot.PRIMARY_MELEE;
		}
	
	@Override
	public void cast(){
final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.COAL));
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 4)){
					if(!BasicUtil.isInTeam(e, p)){  BasicUtil.damage(e, p, 25, DamageType.DARK);;}
				}
				
				FireworkEffect effect = FireworkEffect.builder().withColor(Color.GRAY).withFade(Color.BLACK).with(Type.BALL).trail(true).build();
				
				try {
					CodeEffect.playFirework(item.getWorld(), item.getLocation(), effect);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.STEP_WOOD, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(3));
	}
	
	

}
