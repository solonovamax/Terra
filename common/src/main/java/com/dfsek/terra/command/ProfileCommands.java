package com.dfsek.terra.command;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.profiler.WorldProfiler;
import com.dfsek.terra.command.framework.annotations.Command;
import com.dfsek.terra.command.framework.annotations.SubCommandContainer;
import com.dfsek.terra.config.lang.LangUtil;

@SubCommandContainer(command = "profile", commandAliases = {"p"})
public class ProfileCommands {
    @Command(name = "query", aliases = {"q"}, description = "Get the profiling results")
    public boolean query(TerraPlugin plugin) {
        WorldProfiler profiler = plugin.getWorld(null).getProfiler();
//        sender.sendMessage(profiler.getResultsFormatted());
        return true;
    }

    @Command(name = "reset", aliases = {"r"}, description = "Reset the profiling results")
    public boolean reset(TerraPlugin plugin) {
        WorldProfiler profiler = plugin.getWorld(null).getProfiler();
        profiler.reset();
        LangUtil.send("command.profile.reset", null);
        return true;
    }

    @Command(name = "start", aliases = {"s"}, description = "Start profiling")
    public boolean start(TerraPlugin plugin) {
        WorldProfiler profiler = plugin.getWorld(null).getProfiler();
        profiler.setProfiling(true);
        LangUtil.send("command.profile.start", null);
        return true;
    }
}
