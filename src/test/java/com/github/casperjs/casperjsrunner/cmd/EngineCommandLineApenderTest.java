package com.github.casperjs.casperjsrunner.cmd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

public class EngineCommandLineApenderTest {

    @Test
    public void testAppendWithNull() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getEngine()).thenReturn(null);

        new EngineCommandLineApender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getEngine()).thenReturn("");

        new EngineCommandLineApender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithNonEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getEngine()).thenReturn("anEngine");

        new EngineCommandLineApender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--engine=anEngine");
        verifyNoMoreInteractions(cmdLine);
    }
}
