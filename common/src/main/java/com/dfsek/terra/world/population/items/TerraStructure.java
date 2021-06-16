package com.dfsek.terra.world.population.items;

import com.dfsek.terra.api.math.GridSpawn;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.structures.script.StructureScript;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.config.templates.StructureTemplate;


public class TerraStructure {
    private final ProbabilityCollection<StructureScript> structure;
    private final Range spawnStart;
    private final GridSpawn spawn;
    private final StructureTemplate template;
    
    public TerraStructure(ProbabilityCollection<StructureScript> structures, Range spawnStart, GridSpawn spawn,
                          StructureTemplate template) {
        this.structure = structures;
        this.spawnStart = spawnStart;
        this.spawn = spawn;
        this.template = template;
    }
    
    public GridSpawn getSpawn() {
        return spawn;
    }
    
    public Range getSpawnStart() {
        return spawnStart;
    }
    
    public ProbabilityCollection<StructureScript> getStructure() {
        return structure;
    }
    
    public StructureTemplate getTemplate() {
        return template;
    }
}
