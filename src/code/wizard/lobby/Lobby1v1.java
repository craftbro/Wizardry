package code.wizard.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.v1_7_R3.PacketPlayOutUpdateSign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import code.wizard.item.NamedStack;
import code.wizard.main.Main;
import code.wizard.maps.Arena;

//endgame import

public class Lobby1v1 extends Lobby {

	Inventory shop;
	Inventory aShop;

	HashMap<Player, Arena> arenas = new HashMap<Player, Arena>();

	List<Player> queue = new ArrayList<Player>();

	public Lobby1v1(Main instance) {
		super(instance);
	}

	public void addToQueue(Player p) {
		if (!queue.contains(p)) {
			queue.add(p);
			p.sendMessage(plugin.getPersonalPrefix() + ChatColor.RED
					+ "Added you to the queue");
		} else {
			queue.remove(p);
			p.sendMessage(plugin.getPersonalPrefix() + ChatColor.RED
					+ "Removed you from the queue");
		}
	}

	public void addToQueue(Player p, Sign sign) {
		if (!queue.contains(p)) {
			queue.add(p);
			p.sendMessage(plugin.getPersonalPrefix() + ChatColor.RED
					+ "Added you to the queue");

			String[] lines = new String[] { sign.getLine(0), "Click to leave",
					sign.getLine(2), sign.getLine(3) };

			((CraftPlayer) p).sendSignChange(sign.getLocation(), lines);

		} else {

			String[] lines = new String[] { sign.getLine(0),
					ChatColor.DARK_GRAY + "Click to join", sign.getLine(2),
					sign.getLine(3) };

			((CraftPlayer) p).sendSignChange(sign.getLocation(), lines);

			queue.remove(p);
			p.sendMessage(plugin.getPersonalPrefix() + ChatColor.RED
					+ "Removed you from the queue");
		}
	}

	@Override
	public boolean canBeDamaged(Entity e) {

		if (!(e instanceof Player))
			return false;

		Player p = (Player) e;

		if (arenas.containsKey(p)) {
			if (!arenas.get(p).pp) {
				return true;
			}
		}

		return false;
	}

	public void removeFromList(Player p) {
		arenas.remove(p);
	}

	private boolean emptyArena() {

		for (Arena a : Arena.LIST) {
			if (!a.occupied)
				return true;
		}

		return false;
	}

	private boolean isPlaying(Player p) {
		return arenas.containsKey(p);
	}

	private Arena getEmpty() {

		for (Arena a : Arena.LIST) {
			if (!a.occupied)
				return a;
		}

		return null;
	}

	public Arena getArena(Player p) {
		return arenas.get(p);
	}

	@EventHandler
	public void leave(PlayerQuitEvent event) {
		if (queue.contains(event.getPlayer()))
			queue.remove(event.getPlayer());
	}

	@Override
	public void tick() {

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!isPlaying(p))
				p.setFoodLevel(20);
		}

		for (Arena a : Arena.LIST) {
			if (a.occupied)
				a.tick();
		}

		if (!emptyArena())
			return;

		for (final Player p1 : queue) {
			int index = queue.indexOf(p1);

			if (queue.size() <= 1)
				continue;

			final Player p2 = queue.get(index + 1);

			final Arena a = getEmpty();

			queue.remove(p1);
			queue.remove(p2);

			p1.sendMessage(Main.getPersonalPrefix() + "You're playing against "
					+ ChatColor.RED + p2.getName() + ChatColor.GOLD + " in "
					+ ChatColor.GREEN + a.getName());
			p2.sendMessage(Main.getPersonalPrefix() + "You're playing against "
					+ ChatColor.RED + p1.getName() + ChatColor.GOLD + " in "
					+ ChatColor.GREEN + a.getName());

			p1.sendMessage(Main.getPersonalPrefix()
					+ "Teleporting in 3 seconds...");
			p2.sendMessage(Main.getPersonalPrefix()
					+ "Teleporting in 3 seconds...");

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
					new Runnable() {

						@Override
						public void run() {
							startPlaying(a, p1, p2);
						}
					}, 60);

		}
	}

	public void startPlaying(Arena a, Player p1, Player p2) {
		arenas.put(p1, a);
		arenas.put(p2, a);
		a.start(p1, p2);
	}

	@Override
	protected void setup() {
		lobby = new Location(Bukkit.getWorld("world"), 282, 86, -1059);

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
	public Location getSpawn(Player p) {
		return null;
	}

	@Override
	public Location getSmashSpawn() {
		return null;
	}

	@EventHandler
	public void MOTD(ServerListPingEvent event) {
		event.setMotd(ChatColor.AQUA+"1V1" + ChatColor.GREEN + " Online");
	}

	@Override
	@EventHandler
	public void click(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action a = event.getAction();

		if (started)
			return;
		if (p.getGameMode() == GameMode.CREATIVE)
			return;
		if (isPlaying(p))
			return;

		if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)
			return;

		if (a == Action.RIGHT_CLICK_BLOCK) {
			Block b = event.getClickedBlock();

			if (b.getType() == Material.WALL_SIGN) {
				Sign sign = (Sign) b.getState();

				String s = ChatColor.stripColor(sign.getLine(2));

				if (s.contentEquals("1v1 queue")) {
					addToQueue(p, sign);
					return;
				}

			}
		}

		int slot = p.getInventory().getHeldItemSlot();

		switch (slot) {
		case 1:
			showSpells(p);
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

	public void handleRespawn(Player p) {
		p.setScoreboard(plugin.board);

		if (p.getGameMode() == GameMode.CREATIVE)
			return;

		p.setAllowFlight(false);
		for (Player pl : Bukkit.getOnlinePlayers()) {
			pl.showPlayer(p);
			p.showPlayer(pl);
		}

		p.sendMessage(ChatColor.GREEN + " ");
		p.sendMessage(plugin.getPersonalPrefix() + "Welcome, " + p.getName()
				+ ", to Wizardry v. Beta 1!");
		p.sendMessage(plugin.getPersonalPrefix()
				+ "Please check out the books for more info :)");
		p.sendMessage(ChatColor.GREEN + " ");

		p.setLevel(0);
		p.setExp(0);

		p.getEquipment().setHelmet(new ItemStack(Material.AIR));
		p.getEquipment().setChestplate(new ItemStack(Material.AIR));
		p.getEquipment().setLeggings(new ItemStack(Material.AIR));
		p.getEquipment().setBoots(new ItemStack(Material.AIR));

		p.getInventory().setHeldItemSlot(0);
		p.getInventory().clear();
//		p.getInventory().setItem(
//				1,
//				new NamedStack(ChatColor.AQUA + "" + ChatColor.BOLD
//						+ "Spellbook", Material.BOOK));
//		p.getInventory().setItem(
//				0,
//				new NamedStack(ChatColor.GREEN + "" + ChatColor.BOLD
//						+ "Infobook", Material.BOOK));
//		p.getInventory().setItem(
//				2,
//				new NamedStack(ChatColor.RED + "" + ChatColor.BOLD
//						+ "Yet-To-Be-Addedbook", Material.BOOK));

		p.getInventory().setItem(
				8,
				new NamedStack(ChatColor.GOLD + "" + ChatColor.BOLD
						+ "Switch Spells", Material.NETHER_STAR));
		p.getInventory().setItem(
				7,
				new NamedStack(ChatColor.GOLD + "" + ChatColor.BOLD
						+ "Switch Armor", Material.NETHER_STAR));

	}

}
