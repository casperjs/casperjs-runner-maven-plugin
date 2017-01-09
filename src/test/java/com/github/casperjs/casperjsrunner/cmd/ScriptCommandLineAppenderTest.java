package com.github.casperjs.casperjsrunner.cmd;

import static java.lang.System.getProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

import java.io.File;

public class ScriptCommandLineAppenderTest {

    @Test(expected = MissingScriptFileException.class)
    public void testAppendWithoutScript() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);

        new ScriptCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppend() {
        final File script = new File(getProperty("java.io.tmpdir"), "script.js");
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.getScriptFile()).thenReturn(script);

        new ScriptCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument(script.getAbsolutePath());
        verifyNoMoreInteractions(cmdLine);
    }
}
