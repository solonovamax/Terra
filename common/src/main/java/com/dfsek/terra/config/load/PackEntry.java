package com.dfsek.terra.config.load;

import org.apache.commons.io.FilenameUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;


/**
 * Represents a Terra pack entry..
 */
public class PackEntry implements Closeable {
    private final String name;
    private final InputStream entryInputStream;
    private final boolean isDirectory;
    private final boolean close;
    
    PackEntry(String name, InputStream inputStream, boolean isDirectory, boolean close) {
        this.name = name;
        this.entryInputStream = inputStream;
        this.isDirectory = isDirectory;
        this.close = close;
    }
    
    PackEntry(String name, InputStream inputStream, boolean isDirectory) {
        this(name, inputStream, isDirectory, true);
    }
    
    @Override
    public void close() throws IOException {
        if(close)
            entryInputStream.close();
    }
    
    public InputStream getEntryInputStream() {
        return entryInputStream;
    }
    
    public String getExtension() {
        return FilenameUtils.getExtension(name);
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isClose() {
        return close;
    }
    
    public boolean isDirectory() {
        return isDirectory;
    }
}
