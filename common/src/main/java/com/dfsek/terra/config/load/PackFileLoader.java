package com.dfsek.terra.config.load;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.util.GlueList;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public final class PackFileLoader {
    private final Iterator<PackEntry> entryIterator;
    private final Map<String, Collection<ExceptionalConsumer<PackEntry, ConfigException>>> packEntryProcessors;
    private final ExceptionalFunction<String, PackEntry, FileNotFoundException> singleEntryFinder;
    private final Collection<ExceptionalConsumer<PackEntry, ConfigException>> defaultEntryProcessor;
    
    public PackFileLoader(final File file) throws IOException {
        this(PackUtil.getPackIterator(file), PackUtil.getPackEntryFinder(file));
    }
    
    private PackFileLoader(final Iterator<PackEntry> entryIterator,
                           final ExceptionalFunction<String, PackEntry, FileNotFoundException> singleEntryFinder) {
        this.entryIterator = entryIterator;
        this.singleEntryFinder = singleEntryFinder;
        packEntryProcessors = new HashMap<>();
        defaultEntryProcessor = new GlueList<>();
    }
    
    public void addEntryProcessor(final String extension, final ExceptionalConsumer<PackEntry, ConfigException> entryProcessor) {
        addEntryProcessor("", extension, entryProcessor);
    }
    
    public void addEntryProcessor(final String directory, final String extension,
                                  final ExceptionalConsumer<PackEntry, ConfigException> entryProcessor) {
        Collection<ExceptionalConsumer<PackEntry, ConfigException>> entryProcessors = packEntryProcessors.get(extension);
        
        if(entryProcessors == null)
            //noinspection NestedAssignment
            packEntryProcessors.put(extension, entryProcessors = new GlueList<>());
        
        entryProcessors.add((packEntry -> {
            if(FilenameUtils.normalize(packEntry.getName(), true).startsWith(directory))
                entryProcessor.accept(packEntry);
        }));
    }
    
    public void addDefaultEntryProcessor(final ExceptionalConsumer<PackEntry, ConfigException> entryProcessor) {
        defaultEntryProcessor.add(entryProcessor);
    }
    
    /**
     * Process all entries with the registered functions.
     *
     * @return The amount of entries processed
     *
     * @throws IOException     exceptions while reading files
     * @throws ConfigException exceptions that occur during config loading
     */
    @SuppressWarnings("ForLoopWithMissingComponent")
    public int processEntries() throws IOException, ConfigException {
        int entriesIterated = 0;
        for(; entryIterator.hasNext(); entriesIterated++) {
            entriesIterated++;
            final PackEntry entry = entryIterator.next();
            // find extension
            final String extension = entry.getExtension();
            
            final Collection<ExceptionalConsumer<PackEntry, ConfigException>> entryProcessors =
                    packEntryProcessors.getOrDefault(extension, Collections.emptyList());
            
            for(final ExceptionalConsumer<PackEntry, ConfigException> entryProcessor : entryProcessors)
                entryProcessor.accept(entry);
            
            for(final ExceptionalConsumer<PackEntry, ConfigException> entryProcessor : defaultEntryProcessor)
                entryProcessor.accept(entry);
            
            entry.close();
        }
        return entriesIterated;
    }
    
    /**
     * Expensive operation
     *
     * @param name The name of the entry you wish to find.
     *
     * @return The entry with the specified name.
     */
    public PackEntry getSingleEntry(final String name) throws FileNotFoundException {
        return singleEntryFinder.apply(name);
    }
}
