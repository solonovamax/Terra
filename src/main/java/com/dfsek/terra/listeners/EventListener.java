package com.dfsek.terra.listeners;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.items.tree.TerraTree;
import com.dfsek.terra.registry.TreeRegistry;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;
import org.polydev.gaea.util.FastRandom;

/**
 * Listener for events on all implementations.
 */
public class EventListener implements Listener {
    private final GaeaPlugin main;

    public EventListener(GaeaPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onSaplingGrow(StructureGrowEvent e) {
        if(!TerraWorld.isTerraWorld(e.getWorld())) return;
        TerraWorld tw = TerraWorld.getWorld(e.getWorld());
        ConfigPack c = tw.getConfig();
        if(c.getTemplate().isDisableSaplings()) return;
        e.setCancelled(true);
        Block block = e.getLocation().getBlock();
        BlockData data = block.getBlockData();
        block.setType(Material.AIR);
        TreeRegistry registry = c.getTreeRegistry();
        Tree tree = registry.get(TreeType.fromBukkit(e.getSpecies()).toString());
        Debug.info("Overriding tree type: " + e.getSpecies());
        if(tree instanceof TerraTree) {
            if(!((TerraTree) tree).plantBlockCheck(e.getLocation().subtract(0, 1, 0), new FastRandom())) {
                block.setBlockData(data);
            }
        } else if(!tree.plant(e.getLocation().subtract(0, 1, 0), new FastRandom(), main)) block.setBlockData(data);
    }
}
