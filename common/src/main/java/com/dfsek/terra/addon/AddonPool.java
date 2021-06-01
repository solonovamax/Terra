package com.dfsek.terra.addon;

import com.dfsek.terra.addon.exception.AddonLoadException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class AddonPool {
    private final Map<String, PreLoadAddon> pool = new HashMap<>();
    
    public void add(PreLoadAddon addon) throws AddonLoadException {
        if(pool.containsKey(addon.getId())) {
            String builder = String.format("Duplicate addon ID; Addon with ID %s is loaded by class %s, and requested by class %s.",
                                           addon.getId(), pool.get(addon.getId()).getAddonClass().getCanonicalName(),
                                           addon.getAddonClass().getCanonicalName());
            throw new AddonLoadException(builder);
        }
        pool.put(addon.getId(), addon);
    }
    
    public PreLoadAddon get(String id) {
        return pool.get(id);
    }
    
    public void buildAll() throws AddonLoadException {
        for(PreLoadAddon value : pool.values()) {
            value.rebuildDependencies(this, value, true);
        }
    }
    
    public Set<PreLoadAddon> getAddons() {
        return new HashSet<>(pool.values());
    }
}
