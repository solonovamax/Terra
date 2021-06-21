package com.dfsek.terra.config.load;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;


final class ClosingStreamSpliterator<T extends PackEntry> implements Spliterator<T> {
    private static final Logger logger = LoggerFactory.getLogger(ClosingStreamSpliterator.class);
    
    private final Spliterator<? extends T> wrappedSpliterator;
    private final Stream<? extends T> wrappedStream;
    
    ClosingStreamSpliterator(final Stream<? extends T> wrappedStream) {
        this.wrappedStream = wrappedStream;
        this.wrappedSpliterator = wrappedStream.spliterator();
    }
    
    @Override
    public boolean tryAdvance(final Consumer<? super T> action) {
        return wrappedSpliterator.tryAdvance(closeable -> {
            logger.info("accepting {}.", closeable.getName());
            action.accept(closeable);
            try {
                closeable.close();
                logger.info("Closed {}.", closeable.getName());
            } catch(final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    @Nullable
    @Override
    public Spliterator<T> trySplit() {
        return null;
    }
    
    @Override
    public long estimateSize() {
        return wrappedSpliterator.estimateSize();
    }
    
    @Override
    public int characteristics() {
        return wrappedSpliterator.characteristics();
    }
    
    @Override
    public String toString() {
        return String.format("ClosingStreamSpliterator{wrappedSpliterator=%s}", wrappedSpliterator);
    }
    
    public Spliterator<? extends T> getWrappedSpliterator() {
        return wrappedSpliterator;
    }
    
    public Stream<? extends T> getWrappedStream() {
        return wrappedStream;
    }
}