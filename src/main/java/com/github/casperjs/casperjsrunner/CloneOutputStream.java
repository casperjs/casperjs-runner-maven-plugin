package com.github.casperjs.casperjsrunner;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class CloneOutputStream extends OutputStream {

    private List<OutputStream> streams;

    public CloneOutputStream(final OutputStream... streams) {
        this.streams = newArrayList(streams);
    }

    @Override
    public void write(final int b) throws IOException {
        for (final OutputStream stream : streams) {
            stream.write(b);
        }
    }

    @Override
    public void write(final byte[] b) throws IOException {
        for (final OutputStream stream : streams) {
            stream.write(b);
        }
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        for (final OutputStream stream : streams) {
            stream.write(b, off, len);
        }
    }

    @Override
    public void flush() throws IOException {
        for (final OutputStream stream : streams) {
            stream.flush();
        }
    }

    @Override
    public void close() throws IOException {
        for (final OutputStream stream : streams) {
            stream.close();
        }
    }

}
