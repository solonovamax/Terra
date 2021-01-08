package com.dfsek.terra.command.framework.impl;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.command.framework.annotations.CommandContainer;
import com.dfsek.terra.command.framework.annotations.SubCommandContainer;
import com.dfsek.terra.command.framework.api.Argument;
import com.dfsek.terra.command.framework.api.Command;
import com.dfsek.terra.command.framework.api.CommandManager;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link CommandManager}.
 */
public class CommandManagerImpl implements CommandManager {
    Map<String, Command> commandMap;

    @Override
    public void registerClass(Class<?> clazz) {
        if(clazz.isAnnotationPresent(CommandContainer.class)) {
            CommandContainer container = clazz.getAnnotation(CommandContainer.class);

        } else if(clazz.isAnnotationPresent(SubCommandContainer.class)) {
            SubCommandContainer container = clazz.getAnnotation(SubCommandContainer.class);

            String commandName = container.command();
            String[] commandAliases = container.commandAliases();

            List<MethodCommand> cmds = new GlueList<>();

            Method[] methods = clazz.getMethods();
            List<Command> commands = Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(com.dfsek.terra.command.framework.annotations.Command.class))
                    .map(this::getCommandFromMethod)
                    .collect(Collectors.toList());

            Command cmd = commandMap.get(commandName);
            if(!(cmd instanceof SubCommand))
                throw new IllegalStateException("No duplicate elements allowed.");

            SubCommand command = (SubCommand) cmd;
        }
    }

    @Override
    public Command getCommand(String commandName) {
        return commandMap.get(commandName);
    }

    private Command getCommandFromMethod(Method method) {
        if(method.isAnnotationPresent(com.dfsek.terra.command.framework.annotations.Command.class)) {
            com.dfsek.terra.command.framework.annotations.Command cmd = method.getAnnotation(com.dfsek.terra.command.framework.annotations.Command.class);
            String name = cmd.name();
            String[] aliases = cmd.aliases();
            String description = cmd.description();

            List<Argument> arguments = new GlueList<>();

            List<Parameter> params = Arrays.asList(method.getParameters());

            if(params.isEmpty()) {
                throw new IllegalStateException("The provided method must have at least one parameter.");
            }

            Parameter firstParam = params.remove(0);

            if(firstParam.getType() != TerraPlugin.class)
                throw new IllegalStateException("The first argument must be of type TerraPlugin.");

            for(Parameter parameter : params) {
                if(parameter.isAnnotationPresent(com.dfsek.terra.command.framework.annotations.param.Argument.class)) {

                } else {
                    throw new IllegalStateException("Parameter must have the @Argument annotation.");
                }
            }

//            return new MethodCommand(name, aliases, description, arguments, method);
        } else {
            throw new IllegalStateException("Method must be annotated with @Command.");
        }
    }
}
