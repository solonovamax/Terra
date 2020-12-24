package com.dfsek.terra.command.framework.api;

import com.dfsek.terra.api.platform.TerraPlugin;

import java.util.List;

/**
 * Represents a command that can be run by a user.
 */
public interface Command {
    /**
     * The name of this command.
     *
     * @return The name of this command.
     */
    String getName();

    /**
     * The aliases that belong to this command.
     *
     * @return The aliases for this command.
     */
    String[] getAliases();

    /**
     * The description of this command.
     *
     * @return The description.
     */
    String getDescription();

    /**
     * Gets the next argument based on the current arguments.
     *
     * @param args A list of the current arguments.
     * @return The next argument.
     */
    Argument getNextArgument(List<String> args);

//    /**
//     * A list of arguments for this command.
//     * When running this command via {@link #getAction()}, the action must be supplied with the correct type of arguments in the same order as this list.
//     *
//     * @return This list of arguments for this command.
//     * @see Argument
//     */
//    List<Argument> getArguments();

    /**
     * The action for this command
     *
     * @return The action for this command.
     */
    Action getAction();

    /**
     * A runnable action used for {@link Command}s.
     */
    interface Action {
        /**
         * An empty action that does nothing.
         * Takes any parameters and returns null.
         */
        Action NULL_ACTION = (plugin, parameters) -> true;

        /**
         * The action of this action.
         *
         * @param parameters The parameters with which to run this action.
         * @return Whether or not the action has failed.
         * @throws Exception Could be anything tbh.
         */
        boolean run(TerraPlugin plugin, List<Object> parameters) throws Exception;
    }
}
