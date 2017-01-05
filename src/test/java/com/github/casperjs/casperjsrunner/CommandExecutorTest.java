package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.CommandExecutor.executeCommand;
import static java.lang.System.getProperty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.maven.plugin.logging.Log;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandExecutorTest {

    @BeforeClass
    public static void initLog() throws IOException {
        LogUtils.setLog(mock(Log.class), false);
    }

    @Test
    public void testExecuteWithoutLog() throws ExecuteException, IOException {
        final CommandLine line = mock(CommandLine.class);
        final Map<String, String> envVars = new HashMap<String, String>();
        final Executor executor = mock(Executor.class);
        when(executor.execute(line, envVars)).thenReturn(0);

        assertEquals(0, executeCommand(line, envVars, false, null, executor));

        verify(executor).setExitValues(new int[] { 0, 1 });
        verify(executor).execute(line, envVars);
        verifyNoMoreInteractions(executor);
    }

    @Test
    public void testExecuteWithLog() throws ExecuteException, IOException {
        final CommandLine line = mock(CommandLine.class);
        final Map<String, String> envVars = new HashMap<String, String>();
        final Executor executor = mock(Executor.class);
        when(executor.execute(line, envVars)).thenReturn(0);

        assertEquals(0, executeCommand(line, envVars, false, new File(getProperty("java.io.tmpdir"), "tmp.log"), executor));

        verify(executor).setStreamHandler(any(ExecuteStreamHandler.class));
        verify(executor).setExitValues(new int[] { 0, 1 });
        verify(executor).execute(line, envVars);
        verifyNoMoreInteractions(executor);
    }

    @Test
    public void testExecuteWithEx() throws ExecuteException, IOException {
        final CommandLine line = mock(CommandLine.class);
        final Map<String, String> envVars = new HashMap<String, String>();
        final Executor executor = mock(Executor.class);
        when(executor.execute(line, envVars)).thenThrow(IOException.class);

        assertEquals(-1, executeCommand(line, envVars, false, null, executor));

        verify(executor).setExitValues(new int[] { 0, 1 });
        verify(executor).execute(line, envVars);
        verifyNoMoreInteractions(executor);
    }
}
