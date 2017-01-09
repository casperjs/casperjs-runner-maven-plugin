package com.github.casperjs.casperjsrunner.cmd;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

import java.util.ArrayList;

public class ArgumentsCommandLineAppenderTest {

    @Test
    public void testAppendNull() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getArguments()).thenReturn(null);

        new ArgumentsCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendEmpty() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getArguments()).thenReturn(new ArrayList<String>());

        new ArgumentsCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppend() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getArguments()).thenReturn(newArrayList("--arg1=val1", "--arg2=val2"));

        new ArgumentsCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--arg1=val1", false);
        verify(cmdLine).addArgument("--arg2=val2", false);
        verifyNoMoreInteractions(cmdLine);
    }
}
