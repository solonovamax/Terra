package com.dfsek.terra.api.structures.parser.lang.operations;

import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class BooleanNotOperation extends UnaryOperation<Boolean> {
    public BooleanNotOperation(Returnable<Boolean> input, Position position) {
        super(input, position);
    }

    @Override
    public Boolean apply(Boolean input) {
        return !input;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
}
