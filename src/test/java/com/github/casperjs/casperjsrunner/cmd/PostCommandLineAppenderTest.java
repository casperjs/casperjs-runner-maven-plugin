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

public class PostCommandLineAppenderTest {

    @Test
    public void testAppendWithNull() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getPost()).thenReturn(null);

        new PostCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getPost()).thenReturn("");

        new PostCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithNonEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getPost()).thenReturn("aPostScript");

        new PostCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--post=aPostScript");
        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithDefault() throws IOException {
        File post = null;
        try {
            post = new File(getProperty("java.io.tmpdir"), "post.js");
            post.createNewFile();
            final CommandLine cmdLine = mock(CommandLine.class);
            final Parameters parameters = mock(Parameters.class);
            when(parameters.getTestsDir()).thenReturn(post.getParentFile());

            new PostCommandLineAppender().append(cmdLine, parameters);

            verify(cmdLine).addArgument("--post=" + post.getAbsolutePath());
            verifyNoMoreInteractions(cmdLine);
        } finally {
            if (post != null) {
                post.delete();
            }
        }
    }
}
