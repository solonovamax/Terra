package com.dfsek.terra.config.loaders.palette;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.holder.PaletteHolderBuilder;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class PaletteHolderLoader implements TypeLoader<PaletteHolder> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public PaletteHolder load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        if(!(o instanceof List)) {
            logger.warn("While attempting to load palette holder, provided object was not of the right type.");
            throw new LoadException("Palette holder must be a List of Maps");
        }
        
        List<Map<String, Integer>> palette = (List<Map<String, Integer>>) o;
        PaletteHolderBuilder builder = new PaletteHolderBuilder();
        for(Map<String, Integer> layer : palette) {
            for(Map.Entry<String, Integer> entry : layer.entrySet()) {
                builder.add(entry.getValue(), (Palette) configLoader.loadType(Palette.class, entry.getKey()));
            }
        }
        return builder.build();
    }
}
