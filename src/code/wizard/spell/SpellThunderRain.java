package code.wizard.spell;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;

import code.wizard.util.BasicUtil;
import code.wizard.util.ProjectileWrapper;

public class SpellThunderRain extends Spell{

	public SpellThunderRain(Player p) {
		super(p);
		
		name = ChatColor.YELLOW+"Thunder Rain";
		stack = new ItemStack(Material.GOLD_NUGGET);
		cost = 90;
		
		des.add(ChatColor.DARK_AQUA+"Shoots a compact wave of "+ChatColor.WHITE+"4"+ChatColor.DARK_AQUA+" snowballs");
		des.add(ChatColor.DARK_AQUA+"which, on impact, will all spawn lightning");
		des.add(ChatColor.DARK_AQUA+"dealing "+ChatColor.WHITE+"25"+ChatColor.YELLOW+" Lightning "+ChatColor.DARK_AQUA+"damage to enemies");
		
		info.put("Thunder Damage Range", ChatColor.GREEN+"3");
		
		slot = SpellSlot.PRIMARY_WAND;
	}
	
	@Override
	public void cast(){
		for(int i=0; i<4; i++){
			
			final Snowball s = p.launchProjectile(Snowball.class);
			
			ProjectileWrapper wrapper = new ProjectileWrapper(s);
			
			wrapper.setRunnable(new Runnable(){

				@Override
				public void run() {
				BasicUtil.strikeFakeLightning(s.getLocation(), 25, 3, p);
				}
				
			});
			
		}
		
	}
	
	

}
