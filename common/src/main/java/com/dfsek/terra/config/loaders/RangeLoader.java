package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.Range;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.Map;


@SuppressWarnings("unchecked")
public class RangeLoader implements TypeLoader<Range> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public Range load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        if(!(o instanceof Map)) {
            logger.warn("While attempting to load material set, provided object was not of the right type.");
            throw new LoadException("Range must be a Map with the keys `min` and `max`.");
        }
        
        Map<String, Integer> map = (Map<String, Integer>) o;
        return new Range(map.get("min"), map.get("max"));
    }
}
