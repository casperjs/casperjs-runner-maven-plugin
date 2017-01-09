package com.github.casperjs.casperjsrunner.cmd;

import static com.github.casperjs.casperjsrunner.cmd.CommandLineComputer.computeCmdLine;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.System.getProperty;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

import java.io.File;

public class CommandLineComputerTest {

    @Test
    public void testComputeCommandLine() {
        final CommandLineAppender appender1 = mock(CommandLineAppender.class);
        final CommandLineAppender appender2 = mock(CommandLineAppender.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getCasperRuntime()).thenReturn("casperExec");

        final CommandLine cmd = new CommandLineComputer(newArrayList(appender1, appender2)).compute(parameters);

        assertEquals("casperExec", cmd.getExecutable());

        verify(appender1).append(cmd, parameters);
        verify(appender2).append(cmd, parameters);
        verifyNoMoreInteractions(appender1, appender2);
    }

    @Test
    public void testComputeReal() {
        final File script = new File(getProperty("java.io.tmpdir"), "script.js");
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getCasperRuntime()).thenReturn("casperExec");
        when(parameters.getScriptFile()).thenReturn(script);

        final CommandLine cmd = computeCmdLine(parameters);

        assertEquals("casperExec", cmd.getExecutable());
        assertArrayEquals(new String[] { "test", script.getAbsolutePath() }, cmd.getArguments());
    }
}
