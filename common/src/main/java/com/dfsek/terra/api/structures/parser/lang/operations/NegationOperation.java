package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;


public class NegationOperation extends UnaryOperation<Number> {
    public NegationOperation(Returnable<Number> input, Position position) {
        super(input, position);
    }
    
    @Override
    public Number apply(Number input) {
        return -input.doubleValue();
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
