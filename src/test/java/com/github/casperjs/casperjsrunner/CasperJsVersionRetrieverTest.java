package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.CasperJsVersionRetriever.retrieveVersion;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CasperJsVersionRetrieverTest {

    @Mock
    private Runtime runtime;

    @Mock
    private Process process;

    @Mock
    private InputStream stream;

    @Before
    public void initializeMocks() {
        initMocks(this);

        LogUtils.setLog(mock(Log.class), false);
    }

    @Test
    public void testRetrieveVersion() throws Exception {
        when(runtime.exec("casperjsRuntime --version")).thenReturn(process);
        when(process.getInputStream()).thenReturn(stream);
        when(stream.read(any(byte[].class), anyInt(), anyInt())).then(new Answer<Integer>() {

            private final ByteArrayInputStream innerStream = new ByteArrayInputStream("1.2.3-qualifier".getBytes());

            @Override
            public Integer answer(final InvocationOnMock invocation) throws Throwable {
                return innerStream.read((byte[]) invocation.getArguments()[0], (Integer) invocation.getArguments()[1],
                        (Integer) invocation.getArguments()[2]);
            }
        });

        assertEquals(new DefaultArtifactVersion("1.2.3-qualifier"), retrieveVersion("casperjsRuntime", false, runtime));

        verify(stream).close();
    }

    @Test(expected = MojoFailureException.class)
    public void testRetrieveVersionFacingExWhileRunningCasper() throws Exception {
        when(runtime.exec("casperjsRuntime --version")).thenThrow(new IOException());

        try {
            retrieveVersion("casperjsRuntime", true, runtime);
        } finally {
            verifyNoMoreInteractions(stream);
        }
    }

    @Test(expected = MojoFailureException.class)
    public void testRetrieveVersionFacingExWhileParsingVersion() throws Exception {
        when(runtime.exec("casperjsRuntime --version")).thenReturn(process);
        when(process.getInputStream()).thenReturn(stream);
        when(stream.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException());

        try {
            retrieveVersion("casperjsRuntime", false, runtime);
        } finally {
            verify(stream).close();
        }
    }
}
