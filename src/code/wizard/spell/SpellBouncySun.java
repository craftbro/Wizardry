package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;

public class SpellBouncySun extends Spell{

	public SpellBouncySun(Player p) {
		super(p);
		
		name = ChatColor.GOLD+"Bouncy Sun";
		stack = new ItemStack(Material.DOUBLE_PLANT);
		cost = 75;
		
		des.add(ChatColor.DARK_AQUA+"Shoots a sun that deals "+ChatColor.WHITE+"70"+ChatColor.GOLD+" Light"+" damage on impact");
		des.add(ChatColor.DARK_AQUA+"and it bounce up again if it hits and damages again when landing");
		des.add(ChatColor.DARK_AQUA+"every time it bounce the damage gets reduced by 10");

		info.put("Range", ChatColor.GREEN+"4");
		info.put(ChatColor.GREEN+"Note:", "Damage cant go below 10");
		
		slot = SpellSlot.PRIMARY_WAND;
	}

	
	@Override
	public void cast(){
		final FireworkEffect effect = FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.ORANGE).with(Type.BALL).trail(true).withTrail().withFlicker().build();

		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.DOUBLE_PLANT));
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(1.7));
		Runnable run = new Runnable(){
			int damage = 70;
			public void run() {
				boolean found = false;
				for (LivingEntity e : BasicUtil.getInRadius(ball.getItem().getLocation(), 3)){
					if (!BasicUtil.isInTeam(e, p)){
						found = true;
						BasicUtil.damage(e, p, damage, DamageType.LIGHT);
					}
				}
				if (found){
					damage = damage == 10 ? 10 : damage - 10;
					ball.getItem().remove();
					ball.launch(ball.getItem().getLocation().add(0, 6, 0), new Vector(0, 0.55, 0));
				} else {
					ball.getItem().remove();
				}
			}
		};
		ball.setRunnable(run);
	}
}
