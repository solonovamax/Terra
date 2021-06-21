package com.dfsek.terra.config.load;

import com.google.common.collect.Iterators;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Function;


@SuppressWarnings("unused")
public final class PackUtil {
    private static final Logger logger = LoggerFactory.getLogger(PackUtil.class);
    
    private static final CompressorStreamFactory compressorFactory = new CompressorStreamFactory(false, 256000);
    private static final ArchiveStreamFactory archiveFactory = new ArchiveStreamFactory();
    
    private PackUtil() {}
    
    public static ExceptionalFunction<String, PackEntry, FileNotFoundException> getPackEntryFinder(final File packFile) {
        if(packFile.isDirectory()) {
            return (fileName) -> {
                final File file = new File(packFile, fileName);
                final FileInputStream inputStream = new FileInputStream(file);
                final boolean isDirectory = file.isDirectory();
                
                return new PackEntry(fileName, inputStream, isDirectory);
            };
        } else if(packFile.isFile()) {
            return (fileName) -> {
                try {
                    final InputStream packStream = Files.newInputStream(packFile.toPath());
                    final BufferedInputStream bufferedPackStream = new BufferedInputStream(packStream);
                    
                    Iterator<PackEntry> entryIterator;
                    try {
                        final CompressorInputStream compressedStream = compressorFactory.createCompressorInputStream(bufferedPackStream);
                        final BufferedInputStream bufferedStream = new BufferedInputStream(compressedStream);
                        
                        entryIterator = getArchivePackIterator(archiveType(bufferedStream), bufferedStream);
                    } catch(final CompressorException ignored) {
                        entryIterator = getArchivePackIterator(archiveType(bufferedPackStream), bufferedPackStream);
                    }
                    
                    while(entryIterator.hasNext()) {
                        final PackEntry entry = entryIterator.next();
                        if(fileName.equals(entry.getName()))
                            return entry;
                    }
                    
                    //noinspection ThrowCaughtLocally
                    throw new FileNotFoundException(String.format("Could not find file %s in the pack %s.", fileName, packFile));
                } catch(final FileNotFoundException e) {
                    throw e;
                } catch(final IOException e) {
                    throw new RuntimeException(e);
                }
            };
        } else {
            throw new IllegalArgumentException(String.format("Provided file %s is neither a folder nor a file,", packFile.getPath()));
        }
    }
    
    public static Iterator<PackEntry> getPackIterator(final File packFile) throws IOException {
        if(packFile.isDirectory()) {
            logger.info("Identified config as a folder. Attempting to load config pack '{}'.", packFile.getName());
            
            final Path packPath = packFile.toPath();
            
            return Iterators.transform(FileUtils.iterateFiles(packFile, null, true), unchecked((File file) -> {
                final FileInputStream inputStream = new FileInputStream(file);
                final boolean isDirectory = file.isDirectory();
                final String fileName = file.toPath().relativize(packPath).toString();
                
                return new PackEntry(fileName, inputStream, isDirectory, true);
            })::apply);
        } else if(packFile.isFile()) {
            return getCompressedArchivePackIterator(packFile);
        } else {
            throw new IllegalArgumentException(String.format("Provided file %s is neither a folder nor a file,", packFile.getPath()));
        }
    }
    
    private static Iterator<PackEntry> getCompressedArchivePackIterator(final File file) throws IOException {
        final InputStream packStream = Files.newInputStream(file.toPath());
        final BufferedInputStream bufferedPackStream = new BufferedInputStream(packStream);
        
        try {
            final String compressedType = CompressorStreamFactory.detect(bufferedPackStream);
            final CompressorInputStream compressedStream = compressorFactory.createCompressorInputStream(compressedType,
                                                                                                         bufferedPackStream);
            final BufferedInputStream bufferedStream = new BufferedInputStream(compressedStream);
            
            final String archiveType = archiveType(bufferedStream);
            
            logger.info("Identified config as a {} archive with {} compression. Attempting to load config pack '{}'.",
                        compressedType.toUpperCase(), archiveType.toUpperCase(), file.getName());
            
            return getArchivePackIterator(archiveType, bufferedStream);
        } catch(final CompressorException ignored) {
            final String archiveType = archiveType(bufferedPackStream);
            logger.info("Identified config as a {} archive. Attempting to load config pack '{}'.",
                        archiveType.toUpperCase(), file.getName());
            
            return getArchivePackIterator(archiveType, bufferedPackStream);
        }
    }
    
    private static Iterator<PackEntry> getArchivePackIterator(final String archiveType, final BufferedInputStream packStream)
    throws IOException {
        try {
            final ArchiveInputStream archiveStream = archiveFactory.createArchiveInputStream(archiveType, packStream);
            
            return Spliterators.iterator(new ArchiveStreamSpliterator(archiveStream));
        } catch(final ArchiveException e) {
            throw new IOException(e);
        }
    }
    
    private static String archiveType(final BufferedInputStream inputStream) throws IOException {
        final String archiveType;
        try {
            archiveType = ArchiveStreamFactory.detect(inputStream);
        } catch(final ArchiveException e) {
            throw new IOException(e);
        }
        return archiveType;
    }
    
    private static <F, T> Function<? super F, ? extends T> unchecked(
            final ExceptionalFunction<? super F, ? extends T, ? extends IOException> function) {
        return (t -> {
            try {
                return function.apply(t);
            } catch(final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
