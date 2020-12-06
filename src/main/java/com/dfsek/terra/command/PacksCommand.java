package com.dfsek.terra.command;

import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.registry.ConfigRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.Command;

import java.util.Collections;
import java.util.List;

public class PacksCommand extends Command {
    public PacksCommand(Command parent) {
        super(parent);
    }

    @Override
    public String getName() {
        return "packs";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        ConfigRegistry registry = ConfigRegistry.getRegistry();

        if(registry.entries().size() == 0) {
            LangUtil.send("command.packs.none", commandSender);
            return true;
        }

        LangUtil.send("command.packs.main", commandSender);
        registry.entries().forEach(entry -> {
            ConfigPackTemplate template = entry.getTemplate();
            LangUtil.send("command.packs.pack", commandSender, template.getID(), template.getAuthor(), template.getVersion());
        });

        return true;
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return Collections.emptyList();
    }
}
