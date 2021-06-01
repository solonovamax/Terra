package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.world.population.items.tree.TreeLayer;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class TreeLayerLoader implements TypeLoader<TreeLayer> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public TreeLayer load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        if(!(o instanceof List)) {
            logger.warn("While attempting to load tree layer, provided object was not of the right type.");
            throw new LoadException("Tree layer must be a Map");
        }
        
        Map<String, Object> map = (Map<String, Object>) o;
        double density = ((Number) map.get("density")).doubleValue();
        Range range = configLoader.loadClass(Range.class, map.get("y"));
        if(range == null) {
            logger.warn("You must specify a y-range for the tree.");
            throw new LoadException("Tree range unspecified");
        }
        ProbabilityCollection<Tree> items = (ProbabilityCollection<Tree>) configLoader.loadType(Types.TREE_PROBABILITY_COLLECTION_TYPE,
                                                                                                map.get("items"));
        
        if(map.containsKey("distribution")) {
            NoiseSeeded noise = configLoader.loadClass(NoiseSeeded.class, map.get("distribution"));
            return new TreeLayer(density, range, items, noise.apply(2403L));
        }
        
        return new TreeLayer(density, range, items, new WhiteNoiseSampler(2403));
    }
}
