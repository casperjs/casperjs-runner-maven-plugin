package com.github.casperjs.casperjsrunner.cmd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

public class FailFastCommandLineAppenderTest {

    @Test
    public void testAppendWithoutFailFast() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.isFailFast()).thenReturn(false);

        new FailFastCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithFailFast() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.isFailFast()).thenReturn(true);

        new FailFastCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--fail-fast");
        verifyNoMoreInteractions(cmdLine);
    }
}
