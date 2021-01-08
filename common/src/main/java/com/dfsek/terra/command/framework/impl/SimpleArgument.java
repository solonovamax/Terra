package com.dfsek.terra.command.framework.impl;

import com.dfsek.terra.command.framework.api.Argument;
import org.jetbrains.annotations.Nullable;

public class SimpleArgument implements Argument {
    private final String name;
    private final String description;
    private final String[] suggestions;
    @Nullable
    private final String defaultValue;
    private final Class<?> type;

    public SimpleArgument(String name, String description, String[] suggestions, @Nullable String defaultValue, Class<?> type) {
        this.name = name;
        this.description = description;
        this.suggestions = suggestions;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String[] getSuggestions() {
        return suggestions;
    }

    @Override
    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
