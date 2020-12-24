package com.dfsek.terra.command.framework.api;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an argument for an {@link Command}.
 * Each argument has a corresponding type to which it must be casted when running the command.
 */
public interface Argument {
    /**
     * The name of the command argument.
     *
     * @return The name of the argument.
     */
    String getName();

    /**
     * The description for the usage of this argument.
     *
     * @return The description.
     */
    String getDescription();

    /**
     * An array of suggestions for this argument.
     * Note: this may change over time, so you should always access this every time you need to query the arguments.
     *
     * @return Array containing suggestions for the argument.
     */
    String[] getSuggestions();

    /**
     * The value this argument defaults to if it has no value supplied.
     *
     * @return The default value
     */
    @Nullable
    String getDefaultValue();

    /**
     * The type of this argument.
     * This is the class the argument must be casted to when supplying arguments to a command.
     *
     * @return The type of this argument.
     */
    Class<?> getType();
}
