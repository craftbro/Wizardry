package code.wizard.effect;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkExplosionPlayer {

       /*
       * Class made by BigTeddy98.
       *
       * FireworkExplosionPlayer is a class to display firework explosions to locations or to a single player.
       *
       * 1. No warranty is given or implied.
       * 2. All damage is your own responsibility.
       * 3.If you want to use this in your plugins, a credit would we appreciated.
       */

       //everything needed for the reflection
       private static Constructor<?> packetPlayOutEntityStatus;
       private static Method getEntityHandle;
       private static Field getPlayerConnection;
       private static Method sendPacket;
       private static Method getFireworkHandle;

       static {
               try {
                       //get the constructor of the PacketPlayOutEntityStatus class
                       packetPlayOutEntityStatus = getMCClass("PacketPlayOutEntityStatus").getConstructor(getMCClass("Entity"), byte.class);
                       //get the getHandle method for players
                       getEntityHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
                       //get the field for the playerconnection
                       getPlayerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
                       //get the method for packet sending
                       sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));
                       //get the getHandle method for fireworks
                       getFireworkHandle = getCraftClass("entity.CraftEntity").getMethod("getHandle");
               } catch (Exception e) {
                       e.printStackTrace();
               }
       }

       /*
       * playToPlayer(Player p, Location loc, FireworkEffect fe)
       * Player p - the only player who sees the explosion.
       * Location loc - the location where the explosion happends
       * FireworkEffect fe - the fireworkeffect, example:
       * FireworkEffect.builder().with(Type.BALL).withColor(Color.RED).build()
       */
       public static void playToPlayer(Player p, Location loc, FireworkEffect fe) {
               Object packet = makePacket(loc, fe);
               sendPacket(packet, p);
       }

       /*
       * playToLocation(Location loc, FireworkEffect fe)
       * Location loc - the location where the explosion happends
       * FireworkEffect fe - the fireworkeffect, example:
       * FireworkEffect.builder().with(Type.BALL).withColor(Color.RED).build()
       */
       public static void playToLocation(Location loc, FireworkEffect fe) {
               Object packet = makePacket(loc, fe);
               for (Entity e : loc.getWorld().getEntities()) {
                       if (e instanceof Player) {
                               if (e.getLocation().distance(loc) <= 60) {
                                       sendPacket(packet, (Player) e);
                               }
                       }
               }
       }

       //the method to recieve our packet which we are going to send
       private static Object makePacket(Location loc, FireworkEffect fe) {
               try {
                       Firework firework = loc.getWorld().spawn(loc, Firework.class);
                       FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
                       data.clearEffects();
                       data.setPower(1);
                       data.addEffect(fe);
                       firework.setFireworkMeta(data);
                       Object nms_firework = null;
                       nms_firework = getFireworkHandle.invoke(firework);
                       firework.remove();
                       return packetPlayOutEntityStatus.newInstance(nms_firework, (byte) 17);
               } catch (Exception e) {
                       e.printStackTrace();
               }
               return null;
       }

       //simple method for sending a packet to a CraftPlayer
       private static void sendPacket(Object packet, Player player) {
               try {
                       Object nms_player = getEntityHandle.invoke(player);
                       Object nms_connection = getPlayerConnection.get(nms_player);
                       sendPacket.invoke(nms_connection, packet);
                       System.out.println(player.getName() + " recieved packet.");
               } catch (Exception e) {
                       e.printStackTrace();
               }
       }

       //easy way to get NMS classes
       private static Class<?> getMCClass(String name) throws ClassNotFoundException {
               String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
               String className = "net.minecraft.server." + version + name;
               return Class.forName(className);
       }

       //easy way to get CraftBukkit classes
       private static Class<?> getCraftClass(String name) throws ClassNotFoundException {
               String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
               String className = "org.bukkit.craftbukkit." + version + name;
               return Class.forName(className);
       }
}