package code.wizard.lobby;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.kitteh.tag.TagAPI;

import code.wizard.armor.Armor;
import code.wizard.item.NamedStack;
import code.wizard.main.Main;
import code.wizard.main.Mode;
import code.wizard.maps.Map;
import code.wizard.player.KitManager;
import code.wizard.player.WizTeam;
import code.wizard.special.smash;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition; //endgame import

public class LobbyRandom extends Lobby{

	Location map;
	

	

	Inventory shop;
	Inventory aShop;

	Objective ob;
	
	Objective obr;
	
	public int timeRan = 0; //part of Endgame code
	public boolean endgame = false; //part of Endgame code
	int endDelay = 5;

	public LobbyRandom(Main instance) {
		super(instance);
	}
	

	public static boolean hasStarted() {
		return started;
	}
	
	@Override
	public  boolean canBeDamaged(Entity p){
		return started;
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		if (!started) {
			int count = this.getPlayers();
			
			for(Player p : Bukkit.getOnlinePlayers()){
				p.setFoodLevel(20);
			}
			
			ob.getScore(
					Bukkit.getOfflinePlayer(ChatColor.LIGHT_PURPLE + "Starts In"))
					.setScore(cc);
			ob.getScore(
					Bukkit.getOfflinePlayer(ChatColor.LIGHT_PURPLE + "Players"))
					.setScore(count);

			if (count >= req) {
				if (cc > 1) {
					cc--;
				} else {
					start();
					endDelay = (getPlayers() * 3 > 12 ? 12 : getPlayers() * 3) * 20 * 60;
				}
			}
		} else {
			//part of Endgame code - start
			timeRan++; 
			if (!endgame && timeRan >= (getPlayers() * 2 > 10 ? 10 : getPlayers() * 2)  * 60){
				endgame = true;
				Bukkit.broadcastMessage(Main.getPrefix()+"The Endgame phase has Started! This match is getting too long");
				Bukkit.broadcastMessage(Main.getPrefix()+Condition.ENDGAME.getReminder());
				for (Player p : Bukkit.getOnlinePlayers()){
					BasicUtil.giveCondtition(p, Condition.ENDGAME, 999999);
					if (!spec.contains(p)){
						plugin.find.giveHat(p, "surviving untill endgame", Armor.hat.PATIENCE_MASK);
					}
				}
			}
			//part of Endgame code - start
			if (!pperiod)
				return;
			if (peace > 0) {
				peace--;
			} else {
				Bukkit.broadcastMessage(plugin.getPrefix()
						+ "The battle has begun!");
				pperiod = false;
			}

		}
	}
	
	

	public void end(final Player p) {
		Bukkit.broadcastMessage(plugin.getPrefix() + p.getName() + " Won!");
		
		p.sendMessage(plugin.getPersonalPrefix()+ChatColor.GREEN+"+4 Score for winning!");
		plugin.sql.handler.addScore(p, 4);
		
		giveWin(p);
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if(pl != p && !spec.contains(pl)){
				giveLose(pl);
			}
		}


		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				for (Player pl : Bukkit.getOnlinePlayers()) {
					pl.kickPlayer("Restarting...");
				}

				Bukkit.reload();
			}

		}, 100);

		ended = true;
	}

	public void end(final WizTeam t) {

		Bukkit.broadcastMessage(plugin.getPrefix() + "Team " + t.getName()
				+ ChatColor.DARK_PURPLE + " Won!");
		
		for(Player p : t.players){
			giveWin(p);
			p.sendMessage(plugin.getPersonalPrefix()+ChatColor.GREEN+"+4 Score for winning!");
			plugin.sql.handler.addScore(p, 4);
		}
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if(!t.players.contains(pl) && !spec.contains(pl)) giveLose(pl);
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.kickPlayer("Restarting...");
				}

				Bukkit.reload();
			}

		}, 100);

		ended = true;
	}

	private void start() {
		for(LivingEntity e : map.getWorld().getLivingEntities()) if(e instanceof Sheep) e.remove();
		
		ob.unregister();
		obr.unregister();
		pperiod = true;
		started = true;

		int count = getPlayers();

		if (count % 2 == 0) {
			mode = Mode.TEAM;
			KitManager.setupTeams();
		} else {
			mode = Mode.FFA;
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getGameMode() == GameMode.CREATIVE)
				continue;

			p.teleport(getSpawn(p));

			if (mode == Mode.FFA) {
				p.sendMessage(ChatColor.GREEN
						+ "{---------------------------------------------------}");
				p.sendMessage(plugin.getPersonalPrefix() + ChatColor.GOLD
						+ "How to play Wizardry FFA:");
				p.sendMessage(plugin.getPersonalPrefix()
						+ "Press Q to cycle through the spells");
				p.sendMessage(plugin.getPersonalPrefix()
						+ "Press 1 or 2 to cast the spell in that slot");
				p.sendMessage(plugin.getPersonalPrefix()
						+ "Last man standing wins");
				p.sendMessage(ChatColor.GREEN
						+ "{---------------------------------------------------}");
				p.sendMessage(" ");
			} else if (mode == Mode.TEAM) {
				p.sendMessage(ChatColor.GREEN
						+ "{---------------------------------------------------}");
				p.sendMessage(plugin.getPersonalPrefix() + ChatColor.GOLD
						+ "How to play Wizardry Teams:");
				p.sendMessage(plugin.getPersonalPrefix()
						+ "Press Q to cycle through the spells");
				p.sendMessage(plugin.getPersonalPrefix()
						+ "Press 1 or 2 to cast the spell in that slot");
				p.sendMessage(plugin.getPersonalPrefix()
						+ "Kill the enemy team to win");
				p.sendMessage(ChatColor.GREEN
						+ "{---------------------------------------------------}");
				p.sendMessage(" ");
			}

			WizTeam team = plugin.km.putOnTeam(p);
			TagAPI.refreshPlayer(p);

			plugin.km.giveKit(p, team);

			p.sendMessage(plugin.getPrefix() + "the " + peace
					+ " seconds of peace have begun!");
			
			
		}
		
		int delay = (new Random().nextInt(3)+3)*20*60;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			@Override
			public void run() {
				new smash().spawn(map);
			}
			
		}, delay);

	}

	@Override
	protected void setup() {
		lobby = new Location(Bukkit.getWorld("world"), 282, 86, -1059);
		map = Map.bridge.getSpawn();

		obr = plugin.board.registerNewObjective("rank", "rank");
		obr.setDisplayName(ChatColor.GREEN+"Score: "+ChatColor.GOLD);
		obr.setDisplaySlot(DisplaySlot.BELOW_NAME);
		
		ob = plugin.board.registerNewObjective("lobby", "lobby");

		ob.setDisplayName(ChatColor.LIGHT_PURPLE+"Map "+Map.bridge.getName());
		ob.getScore(Bukkit.getOfflinePlayer(ChatColor.LIGHT_PURPLE + "Players"))
				.setScore(getPlayers());

		ob.setDisplaySlot(DisplaySlot.SIDEBAR);

		shop = Bukkit.createInventory(null, 18, ChatColor.GOLD
				+ "Switch Spells");
		shop.setItem(0, new NamedStack(ChatColor.AQUA + "Primary Melee",
				Material.BOOK));
		shop.setItem(9, new NamedStack(ChatColor.AQUA + "Secundairy Melee",
				Material.BOOK));
		shop.setItem(4, new NamedStack(ChatColor.GREEN + "Primary Stick",
				Material.STICK));
		shop.setItem(13, new NamedStack(ChatColor.GREEN + "Secundairy Stick",
				Material.STICK));
		shop.setItem(8, new NamedStack(ChatColor.GOLD + "Primary Wand",
				Material.GOLD_HOE));
		shop.setItem(17, new NamedStack(ChatColor.GOLD + "Secundairy Wand",
				Material.GOLD_HOE));

		aShop = Bukkit.createInventory(null, 36, ChatColor.GOLD
				+ "Switch Armor");
		aShop.setItem(4, new NamedStack(ChatColor.AQUA + "Helmet",
				Material.LEATHER_HELMET));
		aShop.setItem(13, new NamedStack(ChatColor.AQUA + "Cape",
				Material.LEATHER_CHESTPLATE));
		aShop.setItem(22, new NamedStack(ChatColor.AQUA + "Pants",
				Material.LEATHER_LEGGINGS));
		aShop.setItem(31, new NamedStack(ChatColor.AQUA + "Shoes",
				Material.LEATHER_BOOTS));

	}
	
	@Override
	public Location getSpawn(Player p){
		return map;
	}

	@Override
	public Location getSmashSpawn(){
		return map;
	}


	@EventHandler
	public void MOTD(ServerListPingEvent event) {
		if (!started) {
			event.setMotd(ChatColor.GREEN+"In Lobby");
		} else {
			event.setMotd(ChatColor.DARK_PURPLE+"In Game");
		}

	}

	

	


	public int getPlayers() {
		int pl = 0;

		for (Player p : Bukkit.getWorld("world").getPlayers()) {
		//	p.setFoodLevel(20);

			if (p.getGameMode() != GameMode.CREATIVE) {
				pl++;
			}
		}

		return pl;
	}
	
	@Override
	protected void handleJoin(Player p){
		super.handleJoin(p);
		obr.getScore(p.getName()).setScore(plugin.sql.handler.getScore(p));
	}
	
	@EventHandler
	public void click(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action a = event.getAction();

		if (started)
			return;
		if (p.getGameMode() == GameMode.CREATIVE)
			return;

		if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)
			return;

		int slot = p.getInventory().getHeldItemSlot();

		switch (slot) {
		case 8: {
			p.openInventory(shop);
		}
			break;
		case 7: {
			p.openInventory(aShop);
		}
			break;
		}
	}




}
