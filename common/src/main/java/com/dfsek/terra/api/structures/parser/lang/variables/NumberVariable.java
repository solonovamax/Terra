package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;


public class NumberVariable implements Variable<Number> {
    private final Position position;
    private Number value;
    
    public NumberVariable(Number value, Position position) {
        this.value = value;
        this.position = position;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    @Override
    public Returnable.ReturnType getType() {
        return Returnable.ReturnType.NUMBER;
    }
    
    @Override
    public Number getValue() {
        return value;
    }
    
    @Override
    public void setValue(Number value) {
        this.value = value;
    }
}
