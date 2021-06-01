package com.dfsek.terra.commands.profiler;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.command.CommandTemplate;
import com.dfsek.terra.api.command.annotation.Command;
import com.dfsek.terra.api.command.annotation.type.DebugCommand;
import com.dfsek.terra.api.injection.annotations.Inject;
import com.dfsek.terra.api.platform.CommandSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;


@Command
@DebugCommand
public class ProfileQueryCommand implements CommandTemplate {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Inject
    private TerraPlugin main;
    
    @Override
    public void execute(CommandSender sender) {
        StringBuilder data = new StringBuilder("Terra Profiler data dump: \n");
        main.getProfiler().getTimings().forEach((id, timings) -> data.append(id).append(": ").append(timings.toString()).append('\n'));
        logger.info("Profiler data:\n{}", data);
        sender.sendMessage("Profiler data dumped to console.");
    }
}
