package com.github.casperjs.casperjsrunner.cmd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

public class TestModeCommandLineAppenderTest {

    @Test
    public void testAppend() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);

        new TestModeCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("test");
        verifyNoMoreInteractions(cmdLine);
    }
}
