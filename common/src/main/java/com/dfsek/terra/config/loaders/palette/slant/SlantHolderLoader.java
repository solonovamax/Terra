package com.dfsek.terra.config.loaders.palette.slant;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.slant.SlantHolder;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


@SuppressWarnings("unchecked")
public class SlantHolderLoader implements TypeLoader<SlantHolder> {
    @Override
    public SlantHolder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
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
