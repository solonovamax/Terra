package com.dfsek.terra.addons.structure.command.structure.completer;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.tab.TabCompleter;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.entity.Player;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;

import java.util.ArrayList;
import java.util.List;

public class StructureCompleter implements TabCompleter {
    @Inject
    private TerraPlugin main;

    @Override
    public List<String> complete(CommandSender sender) {
        Player player = (Player) sender;
        return new ArrayList<>(player.world().getConfig().getRegistry(ConfiguredStructure.class).keys());
    }
}