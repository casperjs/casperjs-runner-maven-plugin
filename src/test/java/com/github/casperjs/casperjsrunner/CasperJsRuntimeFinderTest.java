package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.toolchain.CasperjsToolchain.KEY_CASPERJS_TYPE;
import static java.lang.System.setProperty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

@PrepareForTest({ File.class, CasperJsRuntimeFinder.class })
@RunWith(PowerMockRunner.class)
public class CasperJsRuntimeFinderTest {

    @Mock
    private ToolchainManager toolchainManager;

    @Mock
    private Toolchain toolchain;

    @Mock
    private MavenSession session;

    @Before
    public void initializeMocks() {
        initMocks(this);

        LogUtils.setLog(mock(Log.class), false);
    }

    @Test
    public void testFindCasperJsRuntimeFromToolchain() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(toolchain);
        when(toolchain.findTool(KEY_CASPERJS_TYPE)).thenReturn("casperjs runtime from toolchain");

        assertEquals("casperjs runtime from toolchain", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
    }

    @Test
    public void testFindCasperJsRuntimeFromToolchainButOverriden() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(toolchain);
        when(toolchain.findTool(KEY_CASPERJS_TYPE)).thenReturn("casperjs runtime from toolchain");

        assertEquals("casperjs runtime from parameter",
                CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, "casperjs runtime from parameter"));
    }

    @Test
    public void testFindCasperJsRuntimeFromParameter() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);

        assertEquals("casperjs runtime from parameter",
                CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, "casperjs runtime from parameter"));
    }

    @Test
    public void testFindCasperJsRuntimeFromDefault() {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);
        setProperty("os.name", "Linux");

        assertEquals("casperjs", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
    }

    @Test
    public void testFindCasperJsRuntimeFromDefaultOnWindows() throws Exception {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);
        setProperty("os.name", "Windows");
        final File exeFile = mock(File.class);
        final File batFile = mock(File.class);
        whenNew(File.class).withParameterTypes(String.class, String.class).withArguments(anyString(), eq("casperjs.exe")).thenReturn(exeFile);
        whenNew(File.class).withParameterTypes(String.class, String.class).withArguments(anyString(), eq("casperjs.bat")).thenReturn(batFile);
        when(exeFile.exists()).thenReturn(true);
        when(batFile.exists()).thenReturn(false);

        assertEquals("casperjs.exe", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
    }

    @Test
    public void testFindCasperJsRuntimeFromDefaultOnWindowsForOlderCasperjs() throws Exception {
        when(toolchainManager.getToolchainFromBuildContext(KEY_CASPERJS_TYPE, session)).thenReturn(null);
        setProperty("os.name", "Windows");
        final File exeFile = mock(File.class);
        final File batFile = mock(File.class);
        whenNew(File.class).withParameterTypes(String.class, String.class).withArguments(anyString(), eq("casperjs.exe")).thenReturn(exeFile);
        whenNew(File.class).withParameterTypes(String.class, String.class).withArguments(anyString(), eq("casperjs.bat")).thenReturn(batFile);
        when(exeFile.exists()).thenReturn(false);
        when(batFile.exists()).thenReturn(true);

        assertEquals("casperjs.bat", CasperJsRuntimeFinder.findCasperRuntime(toolchainManager, session, null));
    }
}
