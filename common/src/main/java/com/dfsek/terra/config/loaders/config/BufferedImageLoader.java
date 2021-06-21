package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.config.load.PackFileLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Type;


public class BufferedImageLoader implements TypeLoader<BufferedImage> {
    private final PackFileLoader files;
    
    public BufferedImageLoader(final PackFileLoader fileLoader) {
        this.files = fileLoader;
    }
    
    @Override
    public BufferedImage load(final Type t, final Object o, final ConfigLoader loader) throws LoadException {
        try {
            return ImageIO.read(files.getSingleEntry((String) o).getEntryInputStream());
        } catch(final IOException e) {
            throw new LoadException("Unable to load image", e);
        }
    }
}
