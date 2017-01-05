package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.IOUtils.closeQuietly;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCloseNull() {
        closeQuietly(null);
    }

    @Test
    public void testClose() throws IOException {
        final Closeable closeable = mock(Closeable.class);

        closeQuietly(closeable);

        verify(closeable).close();
        verifyNoMoreInteractions(closeable);
    }

    @Test
    public void testCloseWithException() throws IOException {
        final Closeable closeable = mock(Closeable.class);
        doThrow(new IOException()).when(closeable).close();

        closeQuietly(closeable);

        verify(closeable).close();
        verifyNoMoreInteractions(closeable);
    }
}
