package com.github.casperjs.casperjsrunner.cmd;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.junit.Test;

public class VerboseCommandLineAppenderTest {

    @Test
    public void testAppendWithoutVerbose() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        when(parameters.isCasperjsVerbose()).thenReturn(false);

        new VerboseCommandLineAppender().append(cmdLine, parameters);

        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithVerboseAndRecentCasperByMajor() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        final ArtifactVersion version = mock(ArtifactVersion.class);
        when(parameters.isCasperjsVerbose()).thenReturn(true);
        when(parameters.getCasperJsVersion()).thenReturn(version);
        when(version.getMajorVersion()).thenReturn(2);

        new VerboseCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--verbose");
        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithVerboseAndRecentCasperByMinor() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        final ArtifactVersion version = mock(ArtifactVersion.class);
        when(parameters.isCasperjsVerbose()).thenReturn(true);
        when(parameters.getCasperJsVersion()).thenReturn(version);
        when(version.getMajorVersion()).thenReturn(1);
        when(version.getMinorVersion()).thenReturn(1);

        new VerboseCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--verbose");
        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithVerboseAndOldCasperByMajor() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        final ArtifactVersion version = mock(ArtifactVersion.class);
        when(parameters.isCasperjsVerbose()).thenReturn(true);
        when(parameters.getCasperJsVersion()).thenReturn(version);
        when(version.getMajorVersion()).thenReturn(0);

        new VerboseCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--direct");
        verifyNoMoreInteractions(cmdLine);
    }

    @Test
    public void testAppendWithVerboseAndOldCasperByMinor() {
        final CommandLine cmdLine = mock(CommandLine.class);
        final Parameters parameters = mock(Parameters.class);
        final ArtifactVersion version = mock(ArtifactVersion.class);
        when(parameters.isCasperjsVerbose()).thenReturn(true);
        when(parameters.getCasperJsVersion()).thenReturn(version);
        when(version.getMajorVersion()).thenReturn(1);
        when(version.getMinorVersion()).thenReturn(0);

        new VerboseCommandLineAppender().append(cmdLine, parameters);

        verify(cmdLine).addArgument("--direct");
        verifyNoMoreInteractions(cmdLine);
    }
}
