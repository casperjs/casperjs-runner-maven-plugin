package com.github.casperjs.casperjsrunner.toolchain;

import static java.lang.System.getProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.maven.toolchain.MisconfiguredToolchainException;
import org.apache.maven.toolchain.ToolchainPrivate;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.junit.Before;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CasperjsToolchainFactoryTest {

    private CasperjsToolchainFactory factory;

    @Before
    public void initializeFactory() {
        factory = new CasperjsToolchainFactory();
        factory.setLogger(mock(Logger.class));
    }

    @Test
    public void testCreateDefaultToolchain() {
        assertNull(factory.createDefaultToolchain());
    }

    @Test
    public void testCreateToolchainWithNullModel() throws MisconfiguredToolchainException {
        assertNull(factory.createToolchain(null));
    }

    @Test(expected = MisconfiguredToolchainException.class)
    public void testCreateToolchainWithoutExecutable() throws MisconfiguredToolchainException {
        final ToolchainModel model = new ToolchainModel();
        model.setConfiguration(new Xpp3Dom("config"));

        factory.createToolchain(model);
    }

    @Test(expected = MisconfiguredToolchainException.class)
    public void testCreateToolchainWithUnexistingExecutable() throws MisconfiguredToolchainException {
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom casperjsExecutable = new Xpp3Dom("casperjsExecutable");
        casperjsExecutable.setValue("unexistant_executable");
        configuration.addChild(casperjsExecutable);
        final ToolchainModel model = new ToolchainModel();
        model.setConfiguration(configuration);

        factory.createToolchain(model);
    }

    @Test(expected = IncompatibleToolchainAPIException.class)
    public void testCreateToolchainWithBadAPI() throws MisconfiguredToolchainException {
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom casperjsExecutable = new Xpp3Dom("casperjsExecutable");
        casperjsExecutable.setValue(getProperty("java.io.tmpdir"));
        configuration.addChild(casperjsExecutable);
        final ToolchainModel model = mock(ToolchainModel.class);
        when(model.getConfiguration()).thenReturn(configuration);
        when(model.getProvides()).thenThrow(IntrospectionException.class);

        factory.createToolchain(model);
    }

    @Test
    public void testCreateToolchainWithoutVersion() throws MisconfiguredToolchainException {
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom casperjsExecutable = new Xpp3Dom("casperjsExecutable");
        casperjsExecutable.setValue(getProperty("java.io.tmpdir"));
        final Properties props = new Properties();
        props.setProperty("aKey", "aValue");
        configuration.addChild(casperjsExecutable);
        final ToolchainModel model = new ToolchainModel();
        model.setConfiguration(configuration);
        model.setProvides(props);

        final ToolchainPrivate toolchain = factory.createToolchain(model);

        assertEquals(CasperjsToolchain.KEY_CASPERJS_TYPE, toolchain.getType());
        final Map<String, String> requirements = new HashMap<String, String>();
        requirements.put("aKey", "aValue");
        assertTrue(toolchain.matchesRequirements(requirements));
        requirements.put("anotherKey", "anotherValue");
        assertFalse(toolchain.matchesRequirements(requirements));
    }

    @Test
    public void testCreateToolchainWithVersion() throws MisconfiguredToolchainException {
        final Xpp3Dom configuration = new Xpp3Dom("configuration");
        final Xpp3Dom casperjsExecutable = new Xpp3Dom("casperjsExecutable");
        casperjsExecutable.setValue(getProperty("java.io.tmpdir"));
        final Properties props = new Properties();
        props.setProperty("version", "1.1.3");
        configuration.addChild(casperjsExecutable);
        final ToolchainModel model = new ToolchainModel();
        model.setConfiguration(configuration);
        model.setProvides(props);

        final ToolchainPrivate toolchain = factory.createToolchain(model);

        assertEquals(CasperjsToolchain.KEY_CASPERJS_TYPE, toolchain.getType());
        final Map<String, String> requirements = new HashMap<String, String>();
        requirements.put("version", "[1.1.0,1.1.3]");
        assertTrue(toolchain.matchesRequirements(requirements));
    }
}
