package com.github.casperjs.casperjsrunner.cmd;

import static java.lang.System.getProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.junit.Test;

import java.io.File;

public class XUnitCommandLineAppenderTest {

    @Test
    public void testAppendWithoutReports() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);

        new XUnitCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithReports() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.isEnableXmlReports()).thenReturn(true);
        when(parameters.getReportsDir()).thenReturn(new File(getProperty("java.io.tmpdir")));
        when(parameters.getScriptName()).thenReturn("scriptName");

        new XUnitCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--xunit=" + new File(getProperty("java.io.tmpdir"), "TEST-scriptName.xml").getAbsoluteFile());
        verifyNoMoreInteractions(cmdLine);
    }
}
