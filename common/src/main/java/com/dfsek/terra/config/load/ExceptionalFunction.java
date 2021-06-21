package com.dfsek.terra.config.load;

@FunctionalInterface
interface ExceptionalFunction<F, T, E extends Exception> {
    T apply(F t) throws E;
}