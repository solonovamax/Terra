package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.world.population.items.ores.OreConfig;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class OreConfigLoader implements TypeLoader<OreConfig> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public OreConfig load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        if(!(o instanceof List)) {
            logger.warn("While attempting to load ore holder, provided object was not of the right type.");
            throw new LoadException("Ore holder must be a Map");
        }
        
        Map<String, Integer> map = (Map<String, Integer>) o;
        Range amount = new Range(map.get("min"), map.get("max"));
        Range height = new Range(map.get("min-height"), map.get("max-height"));
        return new OreConfig(amount, height);
    }
}
