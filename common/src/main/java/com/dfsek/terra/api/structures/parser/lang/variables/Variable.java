package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;


public interface Variable<T> {
    Position getPosition();
    
    Returnable.ReturnType getType();
    
    T getValue();
    
    void setValue(T value);
}
