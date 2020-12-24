package com.dfsek.terra.command;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.command.framework.annotations.Command;
import com.dfsek.terra.command.framework.annotations.CommandContainer;
import com.dfsek.terra.command.framework.annotations.param.Argument;

@CommandContainer
public class SpawningCommands {
    @Command(name = "ore", aliases = "o", description = "Spawns an ore where you are looking.")
    public boolean ore(TerraPlugin plugin, @Argument(name = "ore", description = "The ore that you want to spawn.") String oreToSpawn) {
        // TODO: 2020-12-22 Implementation
/*
        Block bl = sender.getTargetBlockExact(25);
        OreConfig ore = TerraWorld.getWorld(w).getConfig().getOre(oreToSpawn);
        if(ore == null) {
            LangUtil.send("command.ore.invalid-ore", sender, oreToSpawn);
            return true;
        }
        if(bl == null) {
            LangUtil.send("command.ore.out-of-range", sender);
            return true;
        }
        Vector source = new Vector(FastMath.floorMod(bl.getX(), 16), bl.getY(), FastMath.floorMod(bl.getZ(), 16));
        ore.doVein(source, bl.getChunk(), new FastRandom());
*/
        return true;
    }
}
