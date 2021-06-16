package com.dfsek.terra.api.structures.parser.lang.variables;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;


public class BooleanVariable implements Variable<Boolean> {
    private final Position position;
    private Boolean value;
    
    public BooleanVariable(Boolean value, Position position) {
        this.value = value;
        this.position = position;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
    
    @Override
    public Returnable.ReturnType getType() {
        return Returnable.ReturnType.BOOLEAN;
    }
    
    @Override
    public Boolean getValue() {
        return value;
    }
    
    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
