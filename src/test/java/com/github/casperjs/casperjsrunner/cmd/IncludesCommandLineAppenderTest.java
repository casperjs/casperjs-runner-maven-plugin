package com.github.casperjs.casperjsrunner.cmd;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.System.getProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IncludesCommandLineAppenderTest {

    @Test
    public void testAppendWithNull() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getIncludes()).thenReturn(null);
        when(parameters.getIncludesPatterns()).thenReturn(null);

        new IncludesCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getIncludes()).thenReturn("");

        new IncludesCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithNonEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getIncludes()).thenReturn("anInclude");

        new IncludesCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--includes=anInclude");
        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithEmptyPatterns() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getIncludesPatterns()).thenReturn(new ArrayList<String>());

        new IncludesCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithPatterns() throws IOException {
        final List<File> files = newArrayList();
        try {
            final CommandLine cmdLine = mock(CommandLine.class);
            final Parameters parameters = mock(Parameters.class);
            when(parameters.getIncludesDir()).thenReturn(new File(getProperty("java.io.tmpdir")));
            when(parameters.getIncludesPatterns()).thenReturn(newArrayList("*.js"));
            final File inc1 = new File(getProperty("java.io.tmpdir"), "inc1.js");
            final File inc2 = new File(getProperty("java.io.tmpdir"), "inc2.js");
            inc1.createNewFile();
            inc2.createNewFile();
            files.add(inc1);
            files.add(inc2);

            new IncludesCommandLineAppender().append(cmdLine, parameters);

            verify(cmdLine).addArgument("--includes=" + inc1.getAbsolutePath() + "," + inc2.getAbsolutePath());
            verifyNoMoreInteractions(cmdLine);
        } finally {
            for (final File file : files) {
                file.delete();
            }
        }
    }
}
