package code.wizard.main;

import java.io.File;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import code.configtesting.config.Config;
import code.wizard.effect.CodeEffect;
import code.wizard.lobby.Lobby;
import code.wizard.lobby.LobbyRandom;
import code.wizard.player.Cancels;
import code.wizard.player.FindManager;
import code.wizard.player.KitManager;
import code.wizard.player.WizTeam;
import code.wizard.special.smash;
import code.wizard.spell.SpellLoader;
import code.wizard.sql.SQLBase;
import code.wizard.sql.SQLEvents;
import code.wizard.util.BasicUtil;
import code.wizard.util.Condition;

public class Main extends JavaPlugin{

	private static Main instance = null;
	
	public static Logger log = Bukkit.getLogger();
	
	private static String CONSOLE_PREFIX = "[Wizardry]: ";
	private static String PREFIX = ChatColor.GRAY+"["+ChatColor.DARK_PURPLE+"Wizardry"+ChatColor.GRAY+"]: "+ChatColor.DARK_GREEN;
	private static String PERSONAL_PREFIX = ChatColor.DARK_AQUA+"["+ChatColor.DARK_PURPLE+"Wizardry"+ChatColor.DARK_AQUA+"]: "+ChatColor.GOLD;
	
	public KitManager km;
	public Lobby lobby;
	public SQLBase sql;
	public FindManager find;
	public ServerType type;
	
	public static Scoreboard board;
	
	private  FileConfiguration settings = null;
	
	@Override
	public void onEnable(){
		Main.instance = this;
		
		
		print("Wizardry launching...");

		
		try{			
		launch();
		
		print("Wizardry launched succesfully!");		
		}catch(Exception e){
			print("An error occured:");
			e.printStackTrace();			
		}
		
	}
	
	@Override
	public void onDisable(){
		sql.closeConnection();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args){
		Player p = (Player)sender;
		
		if(p.isOp()){
		if(cmd.equalsIgnoreCase("wizkit")){
		km.giveKit(p, new WizTeam(p.getName()));
		}else if(cmd.equalsIgnoreCase("start")){
		lobby.cc = 1;
			}else if(cmd.equalsIgnoreCase("condition")){
		BasicUtil.giveCondtition(p, Condition.valueOf(args[0]), 10);
			}else if(cmd.equalsIgnoreCase("fw")){
		FireworkEffect effect = FireworkEffect.builder().with(Type.BALL).withColor(Color.BLUE).build();
		
		
			CodeEffect.playFirework(p.getEyeLocation(), effect);
	
			
			}else if(cmd.equalsIgnoreCase("getspells")){
				p.sendMessage(sql.getSpells(p));
				}
			else if(cmd.equalsIgnoreCase("query")){
				List<String> qr = Arrays.asList(args);
				
				String q = "";
				String b = "";
			
				
				for(String r : qr){ q+=r; if(qr.indexOf(r) < qr.size()-1) q+=" ";}
				
				if(q.contains(";")){
					String[] a = q.split(";");
				
				q = a[0];
				b = a[1];
				}
			
				sql.query(p, q, b);
			
				
				
				}else if(cmd.equalsIgnoreCase("smash")){
					new smash().spawn(p.getLocation());
				}else if(cmd.equalsIgnoreCase("health")){
					KitManager.getKit(p).health = Integer.parseInt(args[0]);
				}else if(cmd.equalsIgnoreCase("find")){
					find.findItem(p);
				}
		
		
		}
		
		
		return false;
		
	}
	
	private synchronized void launch(){
		PluginManager pm = Bukkit.getPluginManager();
		
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		
		km = new KitManager(this);
		lobby = new LobbyRandom(this);
		sql = new SQLBase(this);
		find = new FindManager(this);
		
		pm.registerEvents(km , this);
		pm.registerEvents(lobby , this);
		pm.registerEvents(new Cancels() , this);
		pm.registerEvents(new SQLEvents(this) , this);
		
		SpellLoader.initialize();
		
		if(!this.getDataFolder().exists()) this.getDataFolder().mkdir();
		
		File f = new File(this.getDataFolder(), "PlayerData");
		
		if(!f.exists()) f.mkdir();
		
		Config.setFolder(f);
		
		File st = new File(this.getDataFolder(), "settings.yml");
		if(!st.exists())
			try {
				st.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		settings = YamlConfiguration.loadConfiguration(st);
		
		if(!settings.contains("type")){ settings.set("type", "random"); try {
			settings.save(st);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}

		type = ServerType.valueOf(settings.getString("type").toUpperCase());
	
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				tick();
			}
			
		}, 1, 1);
		

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				slowTick();
			}
			
		}, 1, 40);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				secondTick();
			}
			
		}, 1, 20);
	}
	
	public static String getPrefix(){
		return PREFIX;
	}
	
	public Scoreboard getBoard(){
		return board;
	}
	
	public static String getPersonalPrefix(){
		return PERSONAL_PREFIX;
	}
	
	public static void print(String p){
		log.info(CONSOLE_PREFIX+p);
	}
	
	public static Main getInstance(){
		return instance;
	}
	
	private void tick(){
		km.tick();
	}
	
	private void secondTick(){
		lobby.tick();
		km.secondTick();
	}
	
	private void slowTick(){
		km.slowTick();
	}
	
}
