package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;


public class BooleanOrOperation extends BinaryOperation<Boolean, Boolean> {
    public BooleanOrOperation(Returnable<Boolean> left, Returnable<Boolean> right, Position start) {
        super(left, right, start);
    }
    
    @Override
    public Boolean apply(Boolean left, Boolean right) {
        return left || right;
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
