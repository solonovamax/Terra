package com.dfsek.terra.config.prototype;

import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.registry.OpenRegistry;

import java.util.function.Supplier;


public interface ConfigType<T extends ConfigTemplate, R> {
    void callback(ConfigPack pack, TerraPlugin main, T loadedConfig) throws LoadException;
    
    Supplier<OpenRegistry<R>> registrySupplier();
    
    T getTemplate(ConfigPack pack, TerraPlugin main);
    
    Class<R> getTypeClass();
}
