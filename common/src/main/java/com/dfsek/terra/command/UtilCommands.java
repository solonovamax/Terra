package com.dfsek.terra.command;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.command.framework.annotations.Command;
import com.dfsek.terra.command.framework.annotations.CommandContainer;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.registry.ConfigRegistry;

@SuppressWarnings("unused")
@CommandContainer
public class UtilCommands {

    @Command(name = "version", aliases = {"ve", "v"}, description = "Gets the Terra version.")
    public boolean version(TerraPlugin plugin) {
        // TODO: 2020-12-22 Get version somehow + send somehow.
        LangUtil.getLanguage().getMessage("command.version").send(null, plugin.toString());
        return true;
    }

    @Command(name = "reload", aliases = {"r"}, description = "Reloads the Terra pack")
    public boolean reload(TerraPlugin plugin) {
        plugin.getTerraConfig().load(plugin);
        LangUtil.load(plugin.getTerraConfig().getLanguage(), plugin); // Load language.
        if(!plugin.getRegistry().loadAll(plugin)) {
            LangUtil.send("command.reload-error", null);
            return false;
        }
        plugin.reload();
        LangUtil.send("command.reload", null);
        return true;
    }

    @Command(name = "packs", aliases = {"p"}, description = "Lists available Terra packs.")
    public boolean packs(TerraPlugin plugin) {
        ConfigRegistry registry = plugin.getRegistry();

        if(registry.entries().isEmpty()) {
            LangUtil.send("command.packs.none", null);
            return true;
        }

        LangUtil.send("command.packs.main", null);
        registry.entries().forEach(entry -> {
            ConfigPackTemplate template = entry.getTemplate();
            LangUtil.send("command.packs.pack", null, template.getID(), template.getAuthor(), template.getVersion());
        });

        return true;
    }

    @Command(name = "fixChunk", aliases = {"fc"}, description = "Fixes the current chunk.")
    public boolean fixChunk(TerraPlugin plugin) {
        //TerraChunkGenerator.fixChunk(player.getLocation().getChunk());
        return true;
    }

    @Command(name = "saveData", description = "idk man")
    public boolean saveData(TerraPlugin plugin) {
        //TerraChunkGenerator.saveAll();
        LangUtil.send("debug.data-save", null);//, w.getName());
        return true;
    }
}
