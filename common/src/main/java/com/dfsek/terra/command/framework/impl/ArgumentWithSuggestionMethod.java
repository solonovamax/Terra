package com.dfsek.terra.command.framework.impl;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Calls a method to supply suggestions rather than using a static list.
 */
public class ArgumentWithSuggestionMethod extends SimpleArgument {
    private final Method suggestionMethod;

    /**
     * Constructs a new argument that uses a method to generate suggestions.
     *
     * @param name             The name of this argument.
     * @param description      The description of this argument.
     * @param suggestionMethod A method for generating suggestions for this argument.
     * @param defaultValue     A default value for this argument.
     * @param type             The type of this argument.
     * @see SimpleArgument
     */
    public ArgumentWithSuggestionMethod(String name, String description, Method suggestionMethod, @Nullable String defaultValue, Class<?> type) {
        super(name, description, null, defaultValue, type);
        Class<?> returnType = suggestionMethod.getReturnType();
        if(!(returnType.isArray()
                && returnType.getComponentType() == String.class
                && suggestionMethod.getParameterTypes().length == 0
                && Modifier.isStatic(suggestionMethod.getModifiers())))
            this.suggestionMethod = suggestionMethod;
        throw new IllegalStateException("The provided method must return a String[] and must have no arguments & must be static.");
    }

    @Override
    public String[] getSuggestions() {
        try {
            return (String[]) suggestionMethod.invoke(null);
        } catch(IllegalAccessException | InvocationTargetException | ClassCastException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("The method should never produce access exceptions in any form.");
    }
}
