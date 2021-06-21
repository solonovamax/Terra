package com.dfsek.terra.config.load;


@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
