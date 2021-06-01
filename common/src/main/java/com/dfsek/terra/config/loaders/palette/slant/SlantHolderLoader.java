package com.dfsek.terra.config.loaders.palette.slant;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.slant.SlantHolder;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@SuppressWarnings("unchecked")
public class SlantHolderLoader implements TypeLoader<SlantHolder> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public SlantHolder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        if(!(o instanceof List)) {
            logger.warn("While attempting to load slant holder, provided object was not of the right type.");
            throw new LoadException("Slant holder must be a List of Maps");
        }
        
        List<Map<Object, Object>> layers = (List<Map<Object, Object>>) o;
        TreeMap<Double, PaletteHolder> slantLayers = new TreeMap<>();
        double minThreshold = Double.MAX_VALUE;
        
        for(Map<Object, Object> layer : layers) {
            double threshold = ((Number) layer.get("threshold")).doubleValue();
            if(threshold < minThreshold) minThreshold = threshold;
            slantLayers.put(threshold, (PaletteHolder) configLoader.loadType(PaletteHolder.class, layer.get("palette")));
        }
        
        return new SlantHolder(slantLayers, minThreshold);
    }
}
