package com.github.casperjs.casperjsrunner.cmd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

public class LogLevelCommandLineAppenderTest {

    @Test
    public void testAppendWithNull() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getLogLevel()).thenReturn(null);

        new LogLevelCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getLogLevel()).thenReturn("");

        new LogLevelCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithNonEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getLogLevel()).thenReturn("debug");

        new LogLevelCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--log-level=debug");
        verifyNoMoreInteractions(cmdLine);
    }
}
