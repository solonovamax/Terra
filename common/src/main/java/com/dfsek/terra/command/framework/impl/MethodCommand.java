package com.dfsek.terra.command.framework.impl;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.command.framework.api.Argument;
import com.dfsek.terra.command.framework.api.Command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * A command that is a wrapper around a method.
 */
public class MethodCommand implements Command {
    private final String name;
    private final String[] aliases;
    private final String description;
    private final List<Argument> arguments;
    private final Action action;

    public MethodCommand(String name, String[] aliases, String description, List<Argument> arguments, Method method) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.arguments = arguments;
        this.action = new MethodAction(method);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Argument getNextArgument(List<String> args) {
        return args.size() > arguments.size() ? null : arguments.get(args.size() - 1);
    }

    @Override
    public Action getAction() {
        return action;
    }

    public static class MethodAction implements Command.Action {
        private final Method method;

        public MethodAction(Method method) {
            this.method = method;
            if(!(method.getReturnType() == Boolean.class || method.getReturnType() == boolean.class))
                throw new IllegalStateException("Method for action must have a return type of boolean or Boolean.");
            if(!(method.getParameterTypes().length > 0 && method.getParameterTypes()[0] == TerraPlugin.class))
                throw new IllegalStateException("The first parameter of the method must be TerraPlugin.");
            if(!(Modifier.isStatic(method.getModifiers())))
                throw new IllegalStateException("The method must be static.");
        }

        @Override
        public boolean run(TerraPlugin plugin, List<Object> parameters) throws Exception {
            Object[] args = new Object[parameters.size()];
            args[0] = plugin;
            System.arraycopy(parameters.toArray(), 0, args, 1, parameters.size() - 1);
            return (boolean) method.invoke(null, args);
        }
    }

}
