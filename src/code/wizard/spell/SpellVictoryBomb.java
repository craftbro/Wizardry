package code.wizard.spell;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.dsh105.holoapi.HoloAPI;

import code.wizard.effect.CodeEffect;
import code.wizard.player.Kit;
import code.wizard.player.KitManager;
import code.wizard.util.BasicUtil;
import code.wizard.util.DamageType;
import code.wizard.util.ProjectileBall;

public class SpellVictoryBomb extends Spell{

	public SpellVictoryBomb(Player p) {
		super(p);
		
		name = ChatColor.GOLD+"Victory Bomb";
		stack = new ItemStack(Material.GOLD_HELMET);
		cost = 50;
		
		unlockable = true;
		
		des.add(ChatColor.DARK_AQUA+"Fires a bomb that explodes after");
		des.add(ChatColor.WHITE+"0.25"+ChatColor.DARK_AQUA+" seconds, dealing"+ChatColor.WHITE+"30 ");
		des.add(ChatColor.GOLD+"Light "+ChatColor.DARK_AQUA+"damage to enemies if they have less health");
		des.add(ChatColor.DARK_AQUA+"then you, else it deals "+ChatColor.WHITE+"100 "+"damage");
		
		info.put("Range", ChatColor.GREEN+"3");
		
	
		
		slot = SpellSlot.PRIMARY_STICK;
	}
	
	@Override
	public void cast(){
		
		final ProjectileBall ball = new ProjectileBall(new ItemStack(Material.GOLD_HELMET), 5);
		
		ball.setRunnable(new Runnable(){

			@Override
			public void run() {
				
				Item item = ball.getItem();
				
			
				
				for(LivingEntity e : BasicUtil.getInRadius(item.getLocation(), 3)){
					if(!BasicUtil.isInTeam(e, p) && e instanceof Player){ 
						Kit k = KitManager.getKit(p);
						Kit k1 =  KitManager.getKit((Player)e);
						
						if(k.health >= k1.health){
							BasicUtil.damage(e, p, 30, DamageType.LIGHT);
						}else{
							BasicUtil.damage(e, p, 100, DamageType.LIGHT);
							HoloAPI.getManager().createSimpleHologram(e.getEyeLocation().subtract(0, new Random().nextDouble()*1.0, 0), 2, ChatColor.YELLOW+"VICTORY!");
						}
					}
				}
				
				FireworkEffect effect = FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.TEAL).with(Type.BALL).withFlicker().build();
				
				try {
					CodeEffect.playFirework(item.getWorld(), item.getLocation(), effect);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				
				item.remove();
			
				
			}
			
		});
		
		p.getWorld().playSound(p.getLocation(), Sound.BLAZE_HIT, 5, 2);
		
		ball.launch(p.getEyeLocation(), p.getLocation().getDirection().multiply(2));
		
	}
	
	

}
