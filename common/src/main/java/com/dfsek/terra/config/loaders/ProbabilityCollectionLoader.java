package com.dfsek.terra.config.loaders;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class ProbabilityCollectionLoader implements TypeLoader<ProbabilityCollection<Object>> {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Override
    public ProbabilityCollection<Object> load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        ProbabilityCollection<Object> collection = new ProbabilityCollection<>();
        
        if(type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type generic = pType.getActualTypeArguments()[0];
            if(o instanceof Map) {
                Map<Object, Integer> map = (Map<Object, Integer>) o;
                for(Map.Entry<Object, Integer> entry : map.entrySet()) {
                    collection.add(configLoader.loadType(generic, entry.getKey()), entry.getValue());
                }
            } else if(o instanceof List) {
                List<Map<Object, Integer>> map = (List<Map<Object, Integer>>) o;
                for(Map<Object, Integer> l : map) {
                    for(Map.Entry<Object, Integer> entry : l.entrySet()) {
                        if(entry.getValue() == null) throw new LoadException("No probability defined for entry \"" + entry.getKey() + "\"");
                        Object val = configLoader.loadType(generic, entry.getKey());
                        collection.add(val, entry.getValue());
                    }
                }
            } else if(o instanceof String) {
                return new ProbabilityCollection.Singleton<>(configLoader.loadType(generic, o));
            } else {
                logger.warn("While attempting to load probability collection, provided object was not of the right type.");
                throw new LoadException("Malformed Probability Collection: " + o);
            }
        } else {
            throw new LoadException("Unable to load config! Could not retrieve parameterized type: " + type);
        }
        
        return collection;
    }
    
}
