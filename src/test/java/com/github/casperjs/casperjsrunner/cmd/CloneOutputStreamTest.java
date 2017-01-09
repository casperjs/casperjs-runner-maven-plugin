package com.github.casperjs.casperjsrunner.cmd;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.casperjs.casperjsrunner.cmd.CloneOutputStream;

import java.io.IOException;
import java.io.OutputStream;

@RunWith(MockitoJUnitRunner.class)
public class CloneOutputStreamTest {

    @Mock
    private OutputStream out1;

    @Mock
    private OutputStream out2;

    private CloneOutputStream stream;

    @Before
    public void initializeStream() {
        stream = new CloneOutputStream(out1, out2);
    }

    @Test
    public void testWriteInt() throws IOException {
        final int b = 5;

        stream.write(b);

        verify(out1).write(b);
        verify(out2).write(b);
        verifyNoMoreInteractions(out1, out2);
    }

    @Test
    public void testWriteByteArray() throws IOException {
        final byte[] b = new byte[] { 1, 2, 3 };

        stream.write(b);

        verify(out1).write(b);
        verify(out2).write(b);
        verifyNoMoreInteractions(out1, out2);
    }

    @Test
    public void testWriteByteArrayIntInt() throws IOException {
        final byte[] b = new byte[] { -1, -2, -3 };
        final int off = 4;
        final int len = 3;

        stream.write(b, off, len);

        verify(out1).write(b, off, len);
        verify(out2).write(b, off, len);
        verifyNoMoreInteractions(out1, out2);
    }

    @Test
    public void testFlush() throws IOException {
        stream.flush();

        verify(out1).flush();
        verify(out2).flush();
        verifyNoMoreInteractions(out1, out2);
    }

    @Test
    public void testClose() throws IOException {
        stream.close();

        verify(out1).close();
        verify(out2).close();
        verifyNoMoreInteractions(out1, out2);
    }

}
