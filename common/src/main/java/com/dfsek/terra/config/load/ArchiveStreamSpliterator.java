package com.dfsek.terra.config.load;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Spliterator;
import java.util.function.Consumer;


public class ArchiveStreamSpliterator implements Spliterator<PackEntry> {
    private final ArchiveInputStream archiveStream;
    
    ArchiveStreamSpliterator(final ArchiveInputStream archiveStream) {
        this.archiveStream = archiveStream;
    }
    
    @Override
    public boolean tryAdvance(final Consumer<? super PackEntry> action) {
        try {
            ArchiveEntry entry = archiveStream.getNextEntry();
            
            if(!archiveStream.canReadEntryData(entry)) {
                // log something?
                archiveStream.close();
                return false;
            }
            
            if(null != entry)
                action.accept(new PackEntry(entry.getName(), archiveStream, entry.isDirectory(), false));
            else {
                archiveStream.close();
                return false;
            }
            
            return true;
        } catch(final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void forEachRemaining(final Consumer<? super PackEntry> action) {
        //noinspection StatementWithEmptyBody
        while(tryAdvance(action)) {}
    }
    
    @Nullable
    @Override
    public Spliterator<PackEntry> trySplit() {
        return null;
    }
    
    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }
    
    @Override
    public int characteristics() {
        return Spliterator.DISTINCT |
               Spliterator.IMMUTABLE |
               Spliterator.NONNULL |
               Spliterator.ORDERED |
               Spliterator.SUBSIZED;
    }
    
    public ArchiveInputStream getArchiveStream() {
        return archiveStream;
    }
}