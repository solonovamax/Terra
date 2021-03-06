package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.transform.MapTransform;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.registry.TreeRegistry;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

/**
 * Listener for events on all implementations.
 */
public class EventListener implements Listener {
    private final TerraPlugin main;

    public EventListener(TerraPlugin main) {
        this.main = main;
    }

    private static final Transformer<TreeType, String> TREE_TYPE_STRING_TRANSFORMER = new Transformer.Builder<TreeType, String>()
            .addTransform(new MapTransform<TreeType, String>()
                    .add(TreeType.COCOA_TREE, "JUNGLE_COCOA")
                    .add(TreeType.BIG_TREE, "LARGE_OAK")
                    .add(TreeType.TALL_REDWOOD, "LARGE_SPRUCE")
                    .add(TreeType.REDWOOD, "SPRUCE")
                    .add(TreeType.TREE, "OAK")
                    .add(TreeType.MEGA_REDWOOD, "MEGA_SPRUCE")
                    .add(TreeType.SWAMP, "SWAMP_OAK"))
            .addTransform(TreeType::toString).build();

    @EventHandler
    public void onSaplingGrow(StructureGrowEvent e) {
        World bukkit = BukkitAdapter.adapt(e.getWorld());
        if(!TerraWorld.isTerraWorld(bukkit)) return;
        TerraWorld tw = main.getWorld(bukkit);
        ConfigPack c = tw.getConfig();
        if(c.getTemplate().isDisableSaplings()) return;
        e.setCancelled(true);
        Block block = e.getLocation().getBlock();
        BlockData data = block.getBlockData();
        block.setType(Material.AIR);
        TreeRegistry registry = c.getTreeRegistry();
        Tree tree = registry.get(TREE_TYPE_STRING_TRANSFORMER.translate(e.getSpecies()));
        org.bukkit.Location location = e.getLocation();
        if(!tree.plant(new Location(bukkit, location.getX(), location.getY(), location.getZ()), new FastRandom())) block.setBlockData(data);
    }
}
