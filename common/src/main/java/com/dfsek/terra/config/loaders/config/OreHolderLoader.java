package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.world.population.items.ores.Ore;
import com.dfsek.terra.world.population.items.ores.OreConfig;
import com.dfsek.terra.world.population.items.ores.OreHolder;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class OreHolderLoader implements TypeLoader<OreHolder> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public OreHolder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        if(!(o instanceof List)) {
            logger.warn("While attempting to load ore holder, provided object was not of the right type.");
            throw new LoadException("Ore holder must be a Map");
        }
        
        OreHolder holder = new OreHolder();
        Map<String, Object> map = (Map<String, Object>) o;
        
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            holder.add(configLoader.loadClass(Ore.class, entry.getKey()), configLoader.loadClass(OreConfig.class, entry.getValue()),
                       entry.getKey());
        }
        
        return holder;
    }
}
