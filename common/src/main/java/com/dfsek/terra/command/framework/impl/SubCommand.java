package com.dfsek.terra.command.framework.impl;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.command.framework.api.Argument;
import com.dfsek.terra.command.framework.api.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCommand implements Command {
    private final String name;
    private final List<String> aliases;
    private final SubCommandAction subAction;
    private final SubCommandArgument subArgument;

    public SubCommand(Command command) {
        this.name = command.getName();
        this.aliases = new GlueList<>();
        this.subAction = new SubCommandAction();
        this.subArgument = new SubCommandArgument(this.subAction);
        addCommand(command);
    }

    public void addCommand(Command subCommand) {
        subAction.addSubCommand(subCommand);
    }

    public void addAlias(String alias) {
        this.aliases.add(alias);
    }

    public void addAliases(String[] aliases) {
        for(String alias : aliases)
            addAlias(alias);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAliases() {
        return aliases.toArray(new String[0]);
    }

    @Override
    public String getDescription() {
        // TODO: 2020-12-22 handle the multiple descriptions of each command.
        return "";
    }

    @Override
    public Argument getNextArgument(List<String> args) {
        if(args.isEmpty()) {
            return subArgument;
        }
        String firstArg = args.remove(0);

        return subAction.getCommandMap().get(firstArg).getNextArgument(args);
    }

    @Override
    public Action getAction() {
        return subAction;
    }

    public static class SubCommandAction implements Action {
        private final Map<String, Command> commands;

        public SubCommandAction() {
            this.commands = new HashMap<>();
        }

        public Map<String, Command> getCommandMap() {
            return commands;
        }

        public void addSubCommand(Command command) {
            commands.put(command.getName(), command);
            for(String alias : command.getAliases())
                commands.put(alias, command);
        }

        @Override
        public boolean run(TerraPlugin plugin, List<Object> parameters) throws Exception {
            String command = (String) parameters.get(0);
            List<Object> arguments = new GlueList<>(parameters);
            arguments.remove(0);
            return commands.get(command).getAction().run(plugin, arguments);
        }
    }

    public static class SubCommandArgument implements Argument {
        private final SubCommandAction action;

        public SubCommandArgument(SubCommandAction action) {
            this.action = action;
        }

        @Override
        public String getName() {
            return "sub-command";
        }

        @Override
        public String getDescription() {
            return "null";
        }

        @Override
        public String[] getSuggestions() {
            return action.getCommandMap().keySet().toArray(new String[0]);
        }

        @Override
        public String getDefaultValue() {
            return null;
        }

        @Override
        public Class<?> getType() {
            return String.class;
        }
    }
}
