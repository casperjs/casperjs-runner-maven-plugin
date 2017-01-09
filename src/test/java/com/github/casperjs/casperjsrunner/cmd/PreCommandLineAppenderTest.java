package com.github.casperjs.casperjsrunner.cmd;

import static java.lang.System.getProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PreCommandLineAppenderTest {

    @Test
    public void testAppendWithNull() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getPre()).thenReturn(null);

        new PreCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getPre()).thenReturn("");

        new PreCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithNonEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getPre()).thenReturn("aPreScript");

        new PreCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--pre=aPreScript");
        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithDefault() throws IOException {
        File post = null;
        try {
            post = new File(getProperty("java.io.tmpdir"), "pre.js");
            post.createNewFile();
            final CommandLine cmdLine = mock(CommandLine.class);
            final Parameters parameters = mock(Parameters.class);
            when(parameters.getTestsDir()).thenReturn(post.getParentFile());

            new PreCommandLineAppender().append(cmdLine, parameters);

            verify(cmdLine).addArgument("--pre=" + post.getAbsolutePath());
            verifyNoMoreInteractions(cmdLine);
        } finally {
            if (post != null) {
                post.delete();
            }
        }
    }
}
