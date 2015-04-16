package com.github.linsolas.casperjsrunner.toolchain;

import static com.github.linsolas.casperjsrunner.toolchain.CasperjsToolchain.KEY_CASPERJS_EXECUTABLE;
import static com.github.linsolas.casperjsrunner.toolchain.CasperjsToolchain.KEY_CASPERJS_TYPE;

import org.apache.maven.toolchain.MisconfiguredToolchainException;
import org.apache.maven.toolchain.RequirementMatcherFactory;
import org.apache.maven.toolchain.ToolchainFactory;
import org.apache.maven.toolchain.ToolchainPrivate;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import java.io.File;
import java.util.Map.Entry;
import java.util.Properties;

import org.codehaus.plexus.component.annotations.Requirement;
import java.lang.reflect.Method;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * Based on {@code org.apache.maven.toolchain.java.DefaultJavaToolchainFactory}.
 */
@Component(role = ToolchainFactory.class, hint = KEY_CASPERJS_TYPE, description = "A default factory for '"
        + KEY_CASPERJS_TYPE + "' toolchains")
public class DefaultCasperjsToolchainFactory implements ToolchainFactory {

    @Requirement
    private Logger logger;

    @Override
    public ToolchainPrivate createToolchain(final ToolchainModel model)
            throws MisconfiguredToolchainException {
        if (model == null) {
            return null;
        }
        final DefaultCasperjsToolchain toolchain = new DefaultCasperjsToolchain(model, logger);
        final Properties configuration = toProperties((Xpp3Dom) model.getConfiguration());
        final String casperjsExecutable = configuration.getProperty(KEY_CASPERJS_EXECUTABLE);
        if (casperjsExecutable == null) {
            throw new MisconfiguredToolchainException("CasperJS toolchain without the "
                    + KEY_CASPERJS_EXECUTABLE + " configuration element.");
        }
        final String normalizedCasperjsExecutablePath = FileUtils.normalize(casperjsExecutable);
        final File casperjsExecutableFile = new File(normalizedCasperjsExecutablePath);
        if (casperjsExecutableFile.exists()) {
            toolchain.setCasperjsExecutable(normalizedCasperjsExecutablePath);
        } else {
            throw new MisconfiguredToolchainException("Non-existing casperjs executable at "
                    + casperjsExecutableFile.getAbsolutePath());
        }

        // now populate the provides section.
        final Properties provides = getProvidesProperties(model);
        for (final Entry<Object, Object> provide : provides.entrySet()) {
            final String key = (String) provide.getKey();
            final String value = (String) provide.getValue();
            if (value == null) {
                throw new MisconfiguredToolchainException("Provides token '" + key
                        + "' doesn't have any value configured.");
            }
            if ("version".equals(key)) {
                toolchain.addProvideToken(key, RequirementMatcherFactory.createVersionMatcher(value));
            } else {
                toolchain.addProvideToken(key, RequirementMatcherFactory.createExactMatcher(value));
            }
        }
        return toolchain;
    }

    @Override
    public ToolchainPrivate createDefaultToolchain() {
        return null;
    }

    /**
     * Get {@code provides} properties in in a way compatible with toolchains descriptor version 1.0
     * (Maven 2.0.9 to 3.2.3, where it is represented as Object/DOM) and descriptor version 1.1
     * (Maven 3.2.4 and later, where it is represented as Properties).
     *
     * @param model the toolchain model as read from XML
     * @return the properties defined in the {@code provides} element
     * @see <a href="http://jira.codehaus.org/browse/MNG-5718">MNG-5718</a>
     */
    protected static Properties getProvidesProperties(final ToolchainModel model) {
        final Object value = getBeanProperty(model, "provides");
        return value instanceof Properties ? (Properties) value : toProperties((Xpp3Dom) value);
    }

    protected static Properties toProperties(final Xpp3Dom dom) {
        final Properties props = new Properties();
        final Xpp3Dom[] children = dom.getChildren();
        for (final Xpp3Dom child : children) {
            props.setProperty(child.getName(), child.getValue());
        }
        return props;
    }

    protected static Object getBeanProperty(final Object obj, final String property) {
        try {
            final Method method = new PropertyDescriptor(property, obj.getClass()).getReadMethod();
            return method.invoke(obj);
        } catch (final IntrospectionException e) {
            throw new RuntimeException("Incompatible toolchain API", e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Incompatible toolchain API", e);
        } catch (final InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new RuntimeException("Incompatible toolchain API", e);
        }
    }
}
