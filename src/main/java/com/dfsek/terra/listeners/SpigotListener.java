package com.dfsek.terra.listeners;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.async.AsyncStructureFinder;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.items.TerraStructure;
import com.dfsek.terra.util.StructureTypeEnum;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.polydev.gaea.GaeaPlugin;

/**
 * Listener to load on Spigot servers, contains Villager crash prevention and hacky ender eye redirection.
 * <p>
 * (This is currently loaded on all servers; once Paper accepts the StructureLocateEvent PR this will only be loaded on servers without
 * StructureLocateEvent).
 */
public class SpigotListener implements Listener {
    private final GaeaPlugin main;

    public SpigotListener(GaeaPlugin main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderEye(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if(e.getEntityType().equals(EntityType.ENDER_SIGNAL)) {
            Debug.info("Detected Ender Signal...");
            if(!TerraWorld.isTerraWorld(e.getEntity().getWorld())) return;
            TerraWorld tw = TerraWorld.getWorld(e.getEntity().getWorld());
            EnderSignal signal = (EnderSignal) entity;
            TerraStructure config = tw.getConfig().getStructureLocatable(StructureTypeEnum.STRONGHOLD);
            if(config != null) {
                Debug.info("Overriding Ender Signal...");
                AsyncStructureFinder finder = new AsyncStructureFinder(tw.getGrid(), config, e.getLocation(), 0, 500, location -> {
                    if(location != null) signal.setTargetLocation(location.toLocation(signal.getWorld()));
                    Debug.info("Location: " + location);
                }, main);
                finder.run(); // Do this synchronously so eye doesn't change direction several ticks after spawning.
            } else
                main.getLogger().warning("No overrides are defined for Strongholds. Ender Signals will not work correctly.");
        }
    }

    @EventHandler
    public void onCartographerChange(VillagerAcquireTradeEvent e) {
        if(!TerraWorld.isTerraWorld(e.getEntity().getWorld())) return;
        if(!(e.getEntity() instanceof Villager)) return;
        if(((Villager) e.getEntity()).getProfession().equals(Villager.Profession.CARTOGRAPHER))
            e.setCancelled(true); // Cancel leveling if the villager is a Cartographer, to prevent crashing server.
    }

    @EventHandler
    public void onCartographerLevel(VillagerCareerChangeEvent e) {
        if(!TerraWorld.isTerraWorld(e.getEntity().getWorld())) return;
        if(e.getProfession().equals(Villager.Profession.CARTOGRAPHER)) {
            e.getEntity().setProfession(Villager.Profession.NITWIT); // Give villager new profession to prevent server crash.
            e.setCancelled(true);
        }
    }
}
