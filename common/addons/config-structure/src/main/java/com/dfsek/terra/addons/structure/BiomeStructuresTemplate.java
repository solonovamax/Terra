package com.dfsek.terra.addons.structure;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.structure.ConfiguredStructure;

import java.util.Collections;
import java.util.Set;

public class BiomeStructuresTemplate implements ObjectTemplate<BiomeStructures> {
    @Value("structures")
    @Default
    private Set<ConfiguredStructure> structures = Collections.emptySet();

    @Override
    public BiomeStructures get() {
        return new BiomeStructures(structures);
    }
}
