package com.dfsek.terra.command.framework.api;

import com.dfsek.terra.command.framework.annotations.CommandContainer;
import com.dfsek.terra.command.framework.annotations.SubCommandContainer;

/**
 * Manages the {@link Command}s.
 * Used for registering commands & getting the correct one when running it.
 */
public interface CommandManager {
    /**
     * Reflectively analyzes a class for commands and loads all of them.
     * The class provided must have either the annotation {@link CommandContainer}, or {@link SubCommandContainer}.
     *
     * @param clazz The class to register.
     */
    void registerClass(Class<?> clazz);

    /**
     * Registers multiple classes.
     *
     * @param classes The classes to register.
     * @see #registerClass(Class)
     */
    default void registerClasses(Class<?>... classes) {
        for(Class<?> clazz : classes)
            registerClass(clazz);
    }

    /**
     * Gets a command by name.
     *
     * @param commandName The command to retrieve.
     * @return The command.
     */
    Command getCommand(String commandName);
}
