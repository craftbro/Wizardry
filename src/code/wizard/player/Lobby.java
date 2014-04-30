package code.wizard.player;

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
import code.wizard.special.smash;
import code.wizard.spell.Spell;
import code.wizard.spell.SpellLoader;
import code.wizard.spell.SpellSlot;
import code.wizard.spell.SpellVictoryBomb;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition; //endgame import

public class Lobby implements Listener {

	Main plugin;

	Location lobby;
	Location map;

	static boolean started = false;

	Inventory shop;
	Inventory aShop;

	public Mode mode;

	public int cc = 60;
	int req = 2;
	
	public List<Player> spec = new ArrayList<Player>();

	public boolean ended = false;

	int peace = 15;
	boolean pperiod = false;

	Objective ob;
	
	public int timeRan = 0; //part of Endgame code
	public boolean endgame = false; //part of Endgame code
	int endDelay = 5;

	public Lobby(Main instance) {
		plugin = instance;

		setup();
	}

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
			if (!endgame && timeRan >= (getPlayers() * 3.5 > 21 ? 21 : getPlayers() * 3.5) * 20 * 60){
				endgame = true;
				Bukkit.broadcastMessage(Main.getPrefix()+"The Endgame phase has Started! This match is getting too long");
				Bukkit.broadcastMessage(Main.getPrefix()+Condition.ENDGAME.getReminder());
				for (Player p : Bukkit.getOnlinePlayers()){
					if (!spec.contains(p)){
						BasicUtil.giveCondtition(p, Condition.ENDGAME, 999999);
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

			p.teleport(map);

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

	private void setup() {
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

	public static boolean hasStarted() {
		return started;
	}

	@EventHandler
	public void MOTD(ServerListPingEvent event) {
		if (!started) {
			event.setMotd(Main.getPrefix() + "In Lobby");
		} else {
			event.setMotd(Main.getPrefix() + "In Game");
		}

	}

	@EventHandler
	public void click(InventoryClickEvent event) {
		Player p = (Player) event.getWhoClicked();

		if (p.getGameMode() == GameMode.CREATIVE)
			return;

		event.setCancelled(true);

		Inventory inv = event.getInventory();
		ItemStack i = event.getCurrentItem();

		if (inv.getTitle().contains(ChatColor.GOLD + "Switch Spells")) {
			int slot = event.getSlot();

			switch (slot) {
			case 0:
				openSwitchSpells(p, SpellSlot.PRIMARY_MELEE);
				break;
			case 9:
				openSwitchSpells(p, SpellSlot.SECUNDAIRY_MELEE);
				break;
			case 4:
				openSwitchSpells(p, SpellSlot.PRIMARY_STICK);
				break;
			case 13:
				openSwitchSpells(p, SpellSlot.SECUNDAIRY_STICK);
				break;
			case 8:
				openSwitchSpells(p, SpellSlot.PRIMARY_WAND);
				break;
			case 17:
				openSwitchSpells(p, SpellSlot.SECUNDAIRY_WAND);
				break;
			}
		} else if (inv.getTitle().contains(ChatColor.AQUA + "Spells")) {
			if (i.getType() == Material.AIR)
				return;

			Spell s = SpellLoader.loadSpell(p, i);
			Config.setData(p, s.getSlot().getConfigName(),
					ChatColor.stripColor(s.getName()));

			p.sendMessage(plugin.getPersonalPrefix() + s.getName()
					+ ChatColor.GOLD + " Selected!");
		} else if (inv.getTitle().contains(ChatColor.GOLD + "Switch Armor")) {
			int slot = event.getSlot();

			switch (slot) {
			case 4:
				openSwitchArmor(p, 1, "Hats");
				break;
			case 13:
				openSwitchArmor(p, 2, "Capes");
				break;
			case 22:
				openSwitchArmor(p, 3, "Pants");
				break;
			case 31:
				openSwitchArmor(p, 4, "Boots");
				break;
			}
		} else if (inv.getTitle().contains(ChatColor.AQUA + "Hats")) {
			if (i.getType() == Material.AIR)
				return;

			hat h = Armor.hat.getFromStack(i);
			Config.setData(p, "hat", h.name());

			p.sendMessage(plugin.getPersonalPrefix()
					+ i.getItemMeta().getDisplayName() + ChatColor.GOLD
					+ " Selected!");
		} else if (inv.getTitle().contains(ChatColor.AQUA + "Capes")) {
			if (i.getType() == Material.AIR)
				return;

			cape h = Armor.cape.getFromStack(i);
			Config.setData(p, "cape", h.name());

			p.sendMessage(plugin.getPersonalPrefix()
					+ i.getItemMeta().getDisplayName() + ChatColor.GOLD
					+ " Selected!");
		} else if (inv.getTitle().contains(ChatColor.AQUA + "Pants")) {
			if (i.getType() == Material.AIR)
				return;

			pants h = Armor.pants.getFromStack(i);
			Config.setData(p, "pants", h.name());

			p.sendMessage(plugin.getPersonalPrefix()
					+ i.getItemMeta().getDisplayName() + ChatColor.GOLD
					+ " Selected!");
		} else if (inv.getTitle().contains(ChatColor.AQUA + "Boots")) {
			if (i.getType() == Material.AIR)
				return;

			boots h = Armor.boots.getFromStack(i);
			Config.setData(p, "boots", h.name());

			p.sendMessage(plugin.getPersonalPrefix()
					+ i.getItemMeta().getDisplayName() + ChatColor.GOLD
					+ " Selected!");
		}
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
		case 1:
			this.showSpells(p);
			break;
		case 0: {
			p.sendMessage(ChatColor.GOLD
					+ "-----------------------------------------------------");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "Wizardry is a Wizard PVP game");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "There are 3 kinds of spells:");
			p.sendMessage(plugin.getPersonalPrefix() + "Melee, Stick and Wand");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "Switch between those spells using Q");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "Press 1 or 2 to cast the spell in that slot");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "In FFA, the last (wo)man standing wins");
			p.sendMessage(ChatColor.GOLD
					+ "-----------------------------------------------------");
		}
			break;
		case 2: {
			p.sendMessage(ChatColor.RED
					+ "-----------------------------------------------------");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "There will be 5V5 and 1V1 modes");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "There will be Ranking and Global scores");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "There will multiple servers running side-by-side");
			p.sendMessage(plugin.getPersonalPrefix()
					+ "There an advanced matchmaking system");
			p.sendMessage(ChatColor.RED
					+ "-----------------------------------------------------");
		}
			break;
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

	@EventHandler
	public void join(final PlayerJoinEvent event) {
		final Player p = event.getPlayer();

		// File sound = new File(plugin.getDataFolder(), "Join.mp3");
		// BasicUtil.playSound(sound);

		Toolkit.getDefaultToolkit().beep();

		new PlayerDataLoader(p).loadData();

		if (!started) {

			if (!p.isOp()) {
				event.setJoinMessage(plugin.getPrefix() + p.getName()
						+ " joined the lobby");
			} else {
				event.setJoinMessage(plugin.getPrefix() + ChatColor.GOLD
						+ ChatColor.BOLD + "(Admin)" + p.getName()
						+ " joined the lobby");
			}

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
					new Runnable() {

						@Override
						public void run() {
							handleJoin(p);
						}

					}, 1);

		} else {
			// if(p.isOp()) return;
			event.setJoinMessage("");
			p.sendMessage(plugin.getPersonalPrefix() + p.getName()
					+ " please wait for this game to end...");
			kill(p);
			
			spec.add(p);

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
					new Runnable() {

						@Override
						public void run() {
							p.teleport(map);
						}

					}, 1);
		}
	}

	public void kill(Player p) {
		p.teleport(map);
		p.setAllowFlight(true);
		p.getInventory().clear();
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.hidePlayer(p);
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

	private void handleJoin(Player p) {
		p.setScoreboard(plugin.board);

		if (p.getGameMode() == GameMode.CREATIVE)
			return;

		p.teleport(lobby);

		p.setAllowFlight(false);
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.showPlayer(p);
			p.showPlayer(pl);
		}

		p.sendMessage(ChatColor.GREEN + " ");
		p.sendMessage(plugin.getPersonalPrefix() + "Welcome, " + p.getName()
				+ ", to Wizardry v. Aplha 0.1!");
		p.sendMessage(plugin.getPersonalPrefix()
				+ "Please check out the books for more info :)");
		p.sendMessage(ChatColor.GREEN + " ");

		p.getInventory().setHeldItemSlot(0);
		p.getInventory().clear();
		p.getInventory().setItem(
				1,
				new NamedStack(ChatColor.AQUA + "" + ChatColor.BOLD
						+ "Spellbook", Material.BOOK));
		p.getInventory().setItem(
				0,
				new NamedStack(ChatColor.GREEN + "" + ChatColor.BOLD
						+ "Infobook", Material.BOOK));
		p.getInventory().setItem(
				2,
				new NamedStack(ChatColor.RED + "" + ChatColor.BOLD
						+ "Yet-To-Be-Addedbook", Material.BOOK));

		p.getInventory().setItem(
				8,
				new NamedStack(ChatColor.GOLD + "" + ChatColor.BOLD
						+ "Switch Spells", Material.NETHER_STAR));
		p.getInventory().setItem(
				7,
				new NamedStack(ChatColor.GOLD + "" + ChatColor.BOLD
						+ "Switch Armor", Material.NETHER_STAR));

	}

	private void openSwitchSpells(Player p, SpellSlot slot) {
		Inventory i = Bukkit
				.createInventory(null, 9, ChatColor.AQUA + "Spells");

		for (Spell s : SpellLoader.spells.values()) {
			if (s.getSlot() == slot) {
				if (s.isUnlockable() && !plugin.sql.handler.hasSpell(p, s))
					continue;
				i.addItem(s.getStack());
			}
		}

		BasicUtil.open(p, i);
	}

	private void openSwitchArmor(Player p, int n, String s) {
		Inventory i = Bukkit.createInventory(null, 9, ChatColor.AQUA + s);

		switch (n) {
		case 1: {
			for (hat h : Armor.hat.values()) {
				if(h.isUnlockable() && !plugin.sql.handler.hasHat(p, h)) continue;
				i.addItem(h.getStack());
			}
		}
			break;
		case 2: {
			for (cape h : Armor.cape.values()) {
				if(h.isUnlockable() && !plugin.sql.handler.hasCape(p, h)) continue;
				i.addItem(h.getStack());
			}
		}
			break;
		case 3: {
			for (pants h : Armor.pants.values()) {
				if(h.isUnlockable() && !plugin.sql.handler.hasPants(p, h)) continue;
				i.addItem(h.getStack());
			}
		}
			break;
		case 4: {
			for (boots h : Armor.boots.values()) {
				if(h.isUnlockable() && !plugin.sql.handler.hasBoots(p, h)) continue;
				i.addItem(h.getStack());
			}
		}
			break;
		}

		BasicUtil.open(p, i);
	}

	private void showSpells(Player p) {
		Inventory spells = Bukkit.createInventory(null, 27,
				ChatColor.LIGHT_PURPLE + "Current Spells");

		spells.setItem(0, new NamedStack(ChatColor.AQUA + "Melee Spells",
				Material.IRON_SWORD));
		spells.setItem(9, new NamedStack(ChatColor.GREEN + "Stick Spells",
				Material.STICK));
		spells.setItem(18, new NamedStack(ChatColor.GOLD + "Wand Spells",
				Material.GOLD_HOE));

		PlayerDataLoader pdl = new PlayerDataLoader(p);

		pdl.loadData();

		spells.setItem(7, pdl.getPM().getStack());
		spells.setItem(8, pdl.getSM().getStack());
		spells.setItem(16, pdl.getPS().getStack());
		spells.setItem(17, pdl.getSS().getStack());
		spells.setItem(25, pdl.getPW().getStack());
		spells.setItem(26, pdl.getSW().getStack());

		BasicUtil.open(p, spells);
	}

}
