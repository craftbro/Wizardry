package code.wizard.player;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.EntityEffect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import code.wizard.armor.Armor;
import code.wizard.armor.DStats;
import code.wizard.effect.CodeEffect;
import code.wizard.effect.ParticleEffect;
import code.wizard.item.NamedStack;
import code.wizard.lobby.Lobby1v1;
import code.wizard.main.Main;
import code.wizard.main.Mode;
import code.wizard.main.ServerType;
import code.wizard.maps.Arena;
import code.wizard.spell.Spell;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;
import code.wizard.util.DamageType;

import com.dsh105.holoapi.HoloAPI;

public class Kit {

	Main plugin;
	Player p;

	Spell mspell1;
	Spell mspell2;

	Spell sspell1;
	Spell sspell2;

	Spell wspell1;
	Spell wspell2;

	float regen = 0.0035f;

	public double health = 1000;
	public double maxHealth = 1000;
	double oldHealth = 0;


	Scoreboard board;
	Objective ob = null;

	Arena a;

	WizTeam team;

	public DStats stats;

	public HashMap<Condition, Integer> conditions = new HashMap<>();

	int oldLenght = 0;

	public Kit(Main instance, Player p, WizTeam t) {
		plugin = instance;
		this.p = p;
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		team = t;
		
		health = plugin.type == ServerType.RANDOM ? 1000 : 600;
		maxHealth = health;
	}

	public void gearUp() {
		PlayerDataLoader pdl = new PlayerDataLoader(p);
		pdl.loadData();

		ArmorDataLoader adl = new ArmorDataLoader(p);
		adl.loadData();

		loadSpells(pdl);

		if (plugin.lobby.mode == Mode.TEAM) {
			for (Player pl1 : team.players) {
				board.registerNewTeam(pl1.getName());
			}
		} else {
			board.registerNewTeam(p.getName());
		}

		p.setScoreboard(board);

		ob = board.registerNewObjective(
				p.getUniqueId().toString().subSequence(0, 12) + "1", p
						.getUniqueId().toString().subSequence(0, 12)
						+ "1");
		ob.setDisplayName("    " + ChatColor.YELLOW + "["
				+ ChatColor.DARK_PURPLE + "Wizardry" + ChatColor.YELLOW + "]"
				+ "    ");
		ob.setDisplaySlot(DisplaySlot.SIDEBAR);
		ob.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "    ")).setScore(
				60);
		ob.getScore(
				Bukkit.getOfflinePlayer(ChatColor.GREEN + "Your Team"
						+ ChatColor.YELLOW + ":")).setScore(59);

		p.setLevel(0);
		p.setExp(0f);

		PlayerInventory pl = p.getInventory();

		pl.clear();

		pl.setItem(6, new NamedStack(ChatColor.AQUA + "Melee Spells",
				Material.BOOK));
		pl.setItem(7, new NamedStack(ChatColor.GREEN + "Stick Spells",
				Material.STICK));
		pl.setItem(8, new NamedStack(ChatColor.GOLD + "Wand Spells",
				Material.GOLD_HOE));

		pl.setHeldItemSlot(6);

		loadSlot(6, pl);

		loadArmor(adl);

		if (plugin.type == ServerType.ONEVONE) {
			Lobby1v1 lobby = (Lobby1v1) plugin.lobby;
			a = lobby.getArena(p);
		}

	}

	public void end() {
		p.setScoreboard(null);
	}

	public void damage(double d, boolean hurt) {
		if (hurt)
			p.playEffect(EntityEffect.HURT);

		health -= d;
	}

	public void heal(double h) {
		if (!conditions.containsKey(Condition.OIL)) {
			if (health + h > maxHealth)
				h = maxHealth - health;
			health += h;

			HoloAPI.getManager().createSimpleHologram(
					p.getEyeLocation().subtract(
							new Random().nextDouble() * 2.0 - 1, 0.36,
							new Random().nextDouble() * 2.0 - 1), 2,
					new Vector(0, 0.02, 0), ChatColor.GREEN + "+" + h);
		} else {
			HoloAPI.getManager().createSimpleHologram(
					p.getEyeLocation().subtract(
							new Random().nextDouble() * 2.0 - 1, 0.36,
							new Random().nextDouble() * 2.0 - 1), 2,
					ChatColor.DARK_RED + "Heal Blocked!");
		}
	}

	public void secondTick() {
		loadHealth();

		if (p.isSprinting()) {
			p.setFoodLevel(p.getFoodLevel() - 1);
		} else {
			if (p.getFoodLevel() < 20)
				p.setFoodLevel(p.getFoodLevel() + 1);
		}

		if (conditions.containsKey(Condition.SLOW)) {
			p.setWalkSpeed(0.1f);
			p.setSprinting(false);
			p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
					Integer.MAX_VALUE, -100));
		} else {
			p.setWalkSpeed(0.2f);
			p.removePotionEffect(PotionEffectType.JUMP);
		}
		if(conditions.containsKey(Condition.STUN)){
			p.teleport(p); p.setVelocity(p.getVelocity().setX(0).setZ(0));
			new BukkitRunnable(){
				public void run(){
					if(conditions.containsKey(Condition.STUN)){
						p.teleport(p); p.setVelocity(p.getVelocity().setX(0).setZ(0).setY(p.getVelocity().getY() / 2));
					}
				}
			}.runTaskLater(plugin, 10);
		}

		if (p.getLocation().getBlockY() <= 10) {
			if (plugin.lobby.canBeDamaged(p)) {
				health = 0;
			} else {
				p.sendMessage(plugin.getPersonalPrefix() + "Nope, not today!");
				p.teleport(plugin.lobby.getSpawn(p));
			}
		}

		mspell1.tick();
		mspell2.tick();
		sspell1.tick();
		sspell2.tick();
		wspell1.tick();
		wspell2.tick();

		loadSlot(p.getInventory().getHeldItemSlot(), p.getInventory());

	}

	public void tick() {
		if (health > 0) {

			loadBoard();

			if (p.getLocation().getBlockY() >= 73 && !p.isOnGround()) {
				p.setVelocity(new Vector(0, -1, 0));
				ParticleEffect.WITCH_MAGIC.animateAtLocation(p.getEyeLocation()
						.add(0, 0.4, 0), 4, 1);
				p.getWorld().playSound(p.getEyeLocation(), Sound.FIZZ, 2, 2);
			}

			if (plugin.type == ServerType.RANDOM) {
				if (!plugin.lobby.pperiod) {
					if (p.getExp() < 1) {
						p.setExp(p.getExp()
								+ regen /* part of Endgame code - start */
								* (conditions.containsKey(Condition.ENDGAME) ? 2
										: 1)) /* part of Endgame code - end */;
					}
					p.setLevel((int) (p.getExp() * 100));
				} else {
					p.setLevel(plugin.lobby.peace);
				}
			} else {
				Lobby1v1 lobby = (Lobby1v1) plugin.lobby;

				if (!a.pp) {
					if (p.getExp() < 1) {
						p.setExp(p.getExp()
								+ regen /* part of Endgame code - start */
								* (conditions.containsKey(Condition.ENDGAME) ? 2
										: 1)) /* part of Endgame code - end */;
					}
					p.setLevel((int) (p.getExp() * 100));
				} else {
					p.setLevel(a.peace);
				}
			}

		} else {
			

			team.players.remove(p);

			FireworkEffect effect = FireworkEffect.builder()
					.withColor(Color.RED).withFade(Color.BLACK)
					.with(Type.BURST).withFlicker().build();

			try {
				CodeEffect.playFirework(p.getWorld(), p.getLocation(), effect);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			ob.unregister();

			KitManager.unregister(p);

			if (plugin.type != ServerType.ONEVONE) {
				plugin.lobby.kill(p);
				plugin.lobby.spec.add(p);
				Bukkit.broadcastMessage(Main.getPrefix() + p.getName() + " died!");
			} else {
			    a.kill(p);
				
			}

		}
	}

	public void manageSwitch(PlayerItemHeldEvent event) {
		int slot = event.getNewSlot();

		if (slot > 5) {
			loadSlot(slot, p.getInventory());
		} else {
			event.setCancelled(true);
			castSpell(slot, event.getPreviousSlot());
		}
	}

	private void castSpell(int slot1, int slot2) {
		switch (slot2) {

		case 6: {
			switch (slot1) {
			case 0:
				mspell1.use();
				break;
			case 1:
				mspell2.use();
				break;
			}
		}
			break;
		case 7: {
			switch (slot1) {
			case 0:
				sspell1.use();
				break;
			case 1:
				sspell2.use();
				break;
			}
		}
			break;
		case 8: {
			switch (slot1) {
			case 0:
				wspell1.use();
				break;
			case 1:
				wspell2.use();
				break;
			}
		}
			break;

		}
	}

	private void loadSlot(int slot, PlayerInventory pl) {
		switch (slot) {

		case 6: {
			pl.setItem(0, mspell1.getStack());
			pl.setItem(1, mspell2.getStack());
		}
			break;
		case 7: {
			pl.setItem(0, sspell1.getStack());
			pl.setItem(1, sspell2.getStack());
		}
			break;
		case 8: {
			pl.setItem(0, wspell1.getStack());
			pl.setItem(1, wspell2.getStack());
		}
			break;

		}
	}

	public void addCondition(Condition c, int duration) {
		if (conditions.containsKey(c))
			return;
		conditions.put(c, duration);
	}

	public void giveMana(float mana) {
		if (p.getExp() + (mana / 100) > 1)
			mana = (1 - p.getExp()) * 100;

		p.setExp(p.getExp() + (mana / 100));
	}

	public void takeMana(float mana) {
		if (p.getExp() - (mana / 100) < 0)
			mana = p.getExp() * 100;

		p.setExp(p.getExp() - (mana / 100));
	}

	public void onE() {
		switch (p.getInventory().getHeldItemSlot()) {
		case 6:
			p.getInventory().setHeldItemSlot(7);
			break;
		case 7:
			p.getInventory().setHeldItemSlot(8);
			break;
		case 8:
			p.getInventory().setHeldItemSlot(6);
			break;
		}
	}

	public void slowTick() {
		for (Condition c : conditions.keySet()) {
			switch (c) {
			case POISON:
				BasicUtil.damage(p, null, 10, DamageType.POISON);
				break;
			case BURN:
				BasicUtil.damage(p, null, 10, DamageType.FIRE);
				break;
			// part of Endgame code - start
			case ENDGAME:
				BasicUtil.damage(p, null, 5, DamageType.PHYSICAL);
				break;
			// part of Endgame code - end
			case SUPER: {
				for (Location l : BasicUtil.getCircle(p.getEyeLocation()
						.subtract(0, 0.6, 0), 1.5))
					ParticleEffect.RED_DUST.animateAtLocation(l, 1, 1);
			}
				break;
			}

			int i = conditions.get(c);

			if (i > 1) {
				i--;
				conditions.put(c, i);
			} else {
				board.resetScores(Bukkit.getOfflinePlayer(c.getName()));
				conditions.remove(c);
			}

			// if(p.getInventory().getHeldItemSlot() == 6){
			// if(p.getItemInHand().getDurability() > 100){
			// p.getItemInHand().setDurability((short)0);
			// }
			// }

		}
	}

	private void loadHealth() {

		int sc = 57;

		for (Player pl : team.players) {

			if (pl == p)
				continue;
			this.setHealth(pl, sc);
			sc--;
		}

	}

	@SuppressWarnings("deprecation")
	private void loadBoard() {

		if (health != oldHealth) {
			this.setHealth(p, 58);
			oldHealth = health;
		}

		if (oldLenght != conditions.size()) {

			if (conditions.size() > 0) {
				ob.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "    "))
						.setScore(8);
				ob.getScore(
						Bukkit.getOfflinePlayer(ChatColor.LIGHT_PURPLE
								+ "Conditions" + ChatColor.YELLOW + ":"))
						.setScore(7);

				int place = 6;

				for (Condition c : conditions.keySet()) {
					ob.getScore(Bukkit.getOfflinePlayer(c.getName())).setScore(
							place);
					place--;
				}

			} else {

				for (Condition c : Condition.values()) {
					board.resetScores(c.getName());
				}

				board.resetScores(ChatColor.LIGHT_PURPLE + "Conditions"
						+ ChatColor.YELLOW + ":");
				board.resetScores(ChatColor.RED + "    ");

			}
			oldLenght = conditions.size();
		}
	}

	private void setHealth(Player pl, int sc) {

		plugin.print("Setting team health of: " + pl.getName()
				+ " in player class:" + p.getName());

		Team t;

		t = board.getTeam(pl.getName());

		if (t == null) {
			t = board.registerNewTeam(pl.getName());
		}

		Kit kit = KitManager.getKit(pl);

		if (kit == null)
			return;

		OfflinePlayer pl0;

		t.setPrefix(ChatColor.DARK_GREEN + "");

		pl0 = Bukkit.getOfflinePlayer(pl.getName());

		t.setSuffix(ChatColor.YELLOW + ": " + ChatColor.GRAY + kit.health);
		;

		t.addPlayer(pl0);

		ob.getScore(pl0).setScore(sc);
	}

	private void loadArmor(ArmorDataLoader adl) {
		Armor.hat hat = adl.getHat();
		Armor.cape cape = adl.getCape();
		Armor.pants pants = adl.getPants();
		Armor.boots boots = adl.getBoots();

		p.getEquipment().setHelmet(hat.getStack());
		p.getEquipment().setChestplate(cape.getStack());
		p.getEquipment().setLeggings(pants.getStack());
		p.getEquipment().setBoots(boots.getStack());

		stats = new DStats(hat.getStats());
		stats.addStats(cape.getStats());
		stats.addStats(pants.getStats());
		stats.addStats(boots.getStats());

	}

	private void loadSpells(PlayerDataLoader pdl) {
		mspell1 = pdl.getPM();
		mspell2 = pdl.getSM();

		sspell1 = pdl.getPS();
		sspell2 = pdl.getSS();

		wspell1 = pdl.getPW();
		wspell2 = pdl.getSW();
	}

}
