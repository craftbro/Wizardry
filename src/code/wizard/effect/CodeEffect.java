package code.wizard.effect;

import java.lang.reflect.Field;

import java.lang.reflect.Method;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class CodeEffect {

    // internal references, performance improvements
    private static Method world_getHandle = null;
    private static Method nms_world_broadcastEntityEffect = null;
    private static Method firework_getHandle = null;
    
    public static void playFirework(Location loc, FireworkEffect fe) {
    	Firework fw =loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta fm = (FireworkMeta) fw.getFireworkMeta();
        fm.clearEffects();
        fm.addEffect(fe); 
        fw.setFireworkMeta(fm);
    
		 
		 fw.detonate();
    }
    
    /**
     * Play a pretty firework at the location with the FireworkEffect when called
     * @param world
     * @param loc
     * @param fe
     * @throws Exception
     */
    public static void playFirework(World world, Location loc, FireworkEffect fe) throws Exception {
        // Bukkity load (CraftFirework)
        Firework fw = (Firework) world.spawn(loc, Firework.class);
        // the net.minecraft.server.World
        Object nms_world = null;
        Object nms_firework = null;
        /*
         * The reflection part, this gives us access to funky ways of messing around with things
         */
        if(world_getHandle == null) {
            // get the methods of the craftbukkit objects
            world_getHandle = getMethod(world.getClass(), "getHandle");
            firework_getHandle = getMethod(fw.getClass(), "getHandle");
        }
        // invoke with no arguments
        nms_world = world_getHandle.invoke(world, (Object[]) null);
        nms_firework = firework_getHandle.invoke(fw, (Object[]) null);
        // null checks are fast, so having this seperate is ok
        if(nms_world_broadcastEntityEffect == null) {
            // get the method of the nms_world
            nms_world_broadcastEntityEffect = getMethod(nms_world.getClass(), "broadcastEntityEffect");
        }
        /*
         * Now we mess with the metadata, allowing nice clean spawning of a pretty firework (look, pretty lights!)
         */
        // metadata load
        FireworkMeta data = (FireworkMeta) fw.getFireworkMeta();
        // clear existing
        data.clearEffects();
        // power of one
        data.setPower(1);
        // add the effect
        data.addEffect(fe);
        // set the meta
        fw.setFireworkMeta(data);
        /*
         * Finally, we broadcast the entity effect then kill our fireworks object
         */
        // invoke with arguments
        nms_world_broadcastEntityEffect.invoke(nms_world, new Object[] {nms_firework, (byte) 17});
        // remove from the game
        fw.remove();
    }
    
    /**
     * Internal method, used as shorthand to grab our method in a nice friendly manner
     * @param cl
     * @param method
     * @return Method (or null)
     */
    private static Method getMethod(Class<?> cl, String method) {
        for(Method m : cl.getMethods()) {
            if(m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
	
}
	

