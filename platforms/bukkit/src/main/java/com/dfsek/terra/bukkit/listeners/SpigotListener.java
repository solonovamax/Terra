package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.world.locate.AsyncStructureFinder;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.items.TerraStructure;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


/**
 * Listener to load on Spigot servers, contains Villager crash prevention and hacky ender eye redirection.
 * <p>
 * (This is currently loaded on all servers; once Paper accepts the StructureLocateEvent PR this will only be loaded on servers without
 * StructureLocateEvent).
 */
public class SpigotListener implements Listener {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final TerraPlugin main;
    
    public SpigotListener(TerraPlugin main) {
        this.main = main;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderEye(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if(e.getEntityType().equals(EntityType.ENDER_SIGNAL)) {
            logger.debug("Detected Ender Signal...");
            if(!BukkitAdapter.adapt(e.getEntity().getWorld()).isTerraWorld()) return;
            TerraWorld tw = main.getWorld(BukkitAdapter.adapt(e.getEntity().getWorld()));
            EnderSignal signal = (EnderSignal) entity;
            TerraStructure config = tw.getConfig().getRegistry(TerraStructure.class).get(
                    tw.getConfig().getTemplate().getLocatable().get("STRONGHOLD"));
            if(config != null) {
                logger.debug("Overriding Ender Signal...");
                AsyncStructureFinder finder = new AsyncStructureFinder(tw.getBiomeProvider(), config, BukkitAdapter.adapt(e.getLocation()),
                                                                       0, 500, location -> {
                    if(location != null)
                        signal.setTargetLocation(BukkitAdapter.adapt(location.toLocation(BukkitAdapter.adapt(signal.getWorld()))));
                    logger.debug("Location: {}", location);
                }, main);
                finder.run(); // Do this synchronously so eye doesn't change direction several ticks after spawning.
            } else {
                logger.warn("No overrides are defined for Strongholds. Eyes of Ender will not work correctly.");
            }
        }
    }
    
    @EventHandler
    public void onCartographerChange(VillagerAcquireTradeEvent e) {
        AbstractVillager entity = e.getEntity();
        if(BukkitAdapter.adapt(entity.getWorld()).isTerraWorld() && entity instanceof Villager) {
            Villager villager = (Villager) entity;
            if(Profession.CARTOGRAPHER == villager.getProfession()) {
                e.setCancelled(true); // Cancel leveling if the villager is a Cartographer, to prevent crashing server.
                logger.warn(".------------------------------------------------------------------------.\n" +
                            "|     Prevented server crash by stopping Cartographer villager from      |\n" +
                            "|  spawning. Please upgrade to Paper, which has a StructureLocateEvent   |\n" +
                            "|   that fixes this issue at the source, and doesn't require us to do    |\n" +
                            "|                           stupid band-aids.                            |\n" +
                            "|------------------------------------------------------------------------|");
            }
        }
    }
    
    @EventHandler
    public void onCartographerLevel(VillagerCareerChangeEvent e) {
        if(!BukkitAdapter.adapt(e.getEntity().getWorld()).isTerraWorld()) return;
        if(Profession.CARTOGRAPHER == e.getProfession()) {
            e.getEntity().setProfession(Profession.NITWIT); // Give villager new profession to prevent server crash.
            e.setCancelled(true);
            logger.warn(".------------------------------------------------------------------------.\n" +
                        "| Prevented server crash by stopping Cartographer villager from leveling |\n" +
                        "|   up. Please upgrade to Paper, which has a StructureLocateEvent that   |\n" +
                        "|  fixes this issue at the source, and doesn't require us to do stupid   |\n" +
                        "|                               band-aids.                               |\n" +
                        "|------------------------------------------------------------------------|");
        }
    }
}
