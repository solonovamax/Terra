package com.dfsek.terra.api.platform.block.state;

import org.jetbrains.annotations.NotNull;


public interface Sign extends BlockState {
    void setLine(int index, @NotNull String line) throws IndexOutOfBoundsException;
    
    @NotNull String getLine(int index) throws IndexOutOfBoundsException;
    
    @NotNull String[] getLines();
}
