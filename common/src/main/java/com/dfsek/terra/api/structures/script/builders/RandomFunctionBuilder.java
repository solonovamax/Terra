package com.dfsek.terra.api.structures.script.builders;

import com.dfsek.terra.api.structures.parser.exceptions.ParseException;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.functions.FunctionBuilder;
import com.dfsek.terra.api.structures.script.functions.RandomFunction;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;


public class RandomFunctionBuilder implements FunctionBuilder<RandomFunction> {
    @SuppressWarnings("unchecked")
    @Override
    public RandomFunction build(List<Returnable<?>> argumentList, Position position) throws ParseException {
        return new RandomFunction((Returnable<Number>) argumentList.get(0), position);
    }
    
    @Override
    public int argNumber() {
        return 1;
    }
    
    @Override
    public Returnable.ReturnType getArgument(int position) {
        if(position == 0) return Returnable.ReturnType.NUMBER;
        return null;
    }
}
