package code.wizard.lobby;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.kitteh.tag.TagAPI;

import code.configtesting.config.Config;
import code.wizard.armor.Armor;
import code.wizard.armor.Armor.boots;
import code.wizard.armor.Armor.cape;
import code.wizard.armor.Armor.hat;
import code.wizard.armor.Armor.pants;
import code.wizard.item.NamedStack;
import code.wizard.main.Main;
import code.wizard.main.Mode;
import code.wizard.maps.Map;
import code.wizard.player.KitManager;
import code.wizard.player.PlayerDataLoader;
import code.wizard.player.WizTeam;
import code.wizard.special.smash;
import code.wizard.spell.Spell;
import code.wizard.spell.SpellLoader;
import code.wizard.spell.SpellSlot;
import code.wizard.spell.SpellVictoryBomb;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition; //endgame import

public class LobbyRandom extends Lobby{

	



	Location map;

	

	Inventory shop;
	Inventory aShop;

	Objective ob;
	
	public int timeRan = 0; //part of Endgame code
	public boolean endgame = false; //part of Endgame code
	int endDelay = 5;

	public LobbyRandom(Main instance) {
		super(instance);
	}
	

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
			timeRan = timeRan + 1; 
			if (!endgame && timeRan >= (getPlayers() * 3.5 > 21 ? 21 : getPlayers() * 3.5) * 20 * 60){
				endgame = true;
				Bukkit.broadcastMessage(Main.getPrefix()+"The Endgame phase has Started! This match is getting too long");
				Bukkit.broadcastMessage(Main.getPrefix()+Condition.ENDGAME.getReminder());
				for (Player p : Bukkit.getOnlinePlayers()){
					BasicUtil.giveCondtition(p, Condition.ENDGAME, 999999);
					if (!spec.contains(p)){
						plugin.find.giveHat(p, "survived untill endgame", Armor.hat.PATIENCE_MASK);
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
	
	private void giveWin(Player p){
		int wins = (int) plugin.sql.getData(p, "wins");
		wins++;
		plugin.sql.alterData(p, "wins", wins);
		
		if(new Random().nextInt(2) == 0) plugin.find.findItem(p);
		
		if(wins == 1){
			plugin.find.giveSpell(p, "winning for the first time", new SpellVictoryBomb(null));
		}
	}
		
	
	
	private void giveLose(Player p){
		int loses = (int) plugin.sql.getData(p, "loses");
		loses++;
		plugin.sql.alterData(p, "loses", loses);
		
		if(loses == 1){
			plugin.find.givePants(p, "losing for the first time", Armor.pants.LOOSERS_PANTS);
		}
	}

	public void end(final Player p) {
		Bukkit.broadcastMessage(plugin.getPrefix() + p.getName() + " Won!");
		
		giveWin(p);
		
		for (Player pl : Bukkit.getOnlinePlayers()) {
			if(pl != p && !spec.contains(pl)) giveLose(pl);
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
		
		for(Player p : t.players) giveWin(p);
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
		ob.unregister();
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

			WizTeam team;
			team = plugin.km.putOnTeam(p);
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
			event.setMotd(Main.getPrefix() + "In Lobby");
		} else {
			event.setMotd(Main.getPrefix() + "In Game");
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
