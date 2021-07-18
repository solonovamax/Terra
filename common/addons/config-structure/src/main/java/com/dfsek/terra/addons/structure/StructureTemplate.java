package com.dfsek.terra.addons.structure;

import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.StructureSpawn;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class StructureTemplate implements AbstractableTemplate, ConfigTemplate {
    @Value("id")
    @Final
    private String id;

    @Value("scripts")
    private ProbabilityCollection<Structure> structure;

    @Value("spawn.start")
    private Range y;

    @Value("spawn")
    private StructureSpawn spawn;

    public String getID() {
        return id;
    }

    public ProbabilityCollection<Structure> getStructures() {
        return structure;
    }

    public Range getY() {
        return y;
    }

    public StructureSpawn getSpawn() {
        return spawn;
    }
}
