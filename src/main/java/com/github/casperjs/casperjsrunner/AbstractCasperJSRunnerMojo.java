package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.ArgQuoter.quote;
import static com.github.casperjs.casperjsrunner.CasperJsRuntimeFinder.findCasperRuntime;
import static com.github.casperjs.casperjsrunner.CasperJsVersionRetriever.retrieveVersion;
import static com.github.casperjs.casperjsrunner.CommandExecutor.executeCommand;
import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;
import static com.github.casperjs.casperjsrunner.PathToNameBuilder.buildName;
import static com.github.casperjs.casperjsrunner.PatternsChecker.checkPatterns;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.toolchain.ToolchainManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Runs JavaScript and/or CoffeScript test files on CasperJS instance
 *
 * @author Romain Linsolas
 * @author Benoit Guerin
 * @since 09/04/13
 */
public abstract class AbstractCasperJSRunnerMojo extends AbstractMojo {

    // Parameters for the plugin

    /**
     * Complete path of the executable for CasperJS. <br/>
     * <b>Default value:</b> Found from <a href="http://maven.apache.org/guides/mini/guide-using-toolchains.html">toolchain</a> named
     * <b><i>casperjs</b></i>, then from this parameter, then from PATH with default value of <b>casperjs</b> on Linux/Mac or <b>casperjs.exe</b>
     * (<b>casperjs.bat</b> for compatibility with old versions of CasperJS) on Windows
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.executable")
    private String casperExecPath;

    /**
     * Directory where the tests to execute are stored. <br/>
     * If <code>${tests.directory}/includes</code> and <code>${tests.directory}/scripts</code> directories exist, this is changed to
     * <code>${tests.directory}/scripts</code> and all <code>*.js</code> files in <code>${tests.directory}/includes</code> will automatically be added
     * to the CasperJS <code>--includes</code> list.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.tests.directory", defaultValue = "${basedir}/src/test/casperjs")
    private File testsDir;

    /**
     * Specify this parameter to run individual tests by file name, overriding the <code>testIncludes</code>/<code>testExcludes</code> parameters.
     * Each pattern you specify here will be used to create an include pattern formatted like <code>**&#47;${test}.{js,coffee}</code>, so you can just
     * type "-Dtest=MyTest" to run a single test called <code>foo/MyTest.js</code> or <code>foo/MyTest.coffee</code>.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.test")
    private String test;

    /**
     * A list of <code>&lt;testsInclude&gt;</code> elements specifying the tests (by pattern) that should be included in testing. <br/>
     * <b>Default value:</b> When not specified and when the test parameter is not specified, the default includes will be (javascript patterns will
     * only be set if <code>includeJS</code> is <code>true</code>, and coffee patterns will only be set if <code>includeCS</code> is <code>true</code>
     * ) <br/>
     * <br/>
     * <code>&lt;testsIncludes&gt;<br/>
    &nbsp;&nbsp;&lt;testsInclude&gt;**&#47;Test*.js&lt;/testsInclude&gt;<br/>
    &nbsp;&nbsp;&lt;testsInclude&gt;**&#47;*Test.js&lt;/testsInclude&gt;<br/>
    &nbsp;&nbsp;&lt;testsInclude&gt;**&#47;*TestCase.js&lt;/testsInclude&gt;<br/>
    &nbsp;&nbsp;&lt;testsInclude&gt;**&#47;Test*.coffee&lt;/testsInclude&gt;<br/>
    &nbsp;&nbsp;&lt;testsInclude&gt;**&#47;*Test.coffee&lt;/testsInclude&gt;<br/>
    &nbsp;&nbsp;&lt;testsInclude&gt;**&#47;*TestCase.coffee&lt;/testsInclude&gt;<br/>
    &lt;/testsIncludes&gt;</code>
     *
     * @since 1.0.1
     */
    @Parameter
    private List<String> testsIncludes;

    /**
     * A list of <code>&lt;testsExclude&gt;</code> elements specifying the tests (by pattern) that should be excluded in testing.
     *
     * @since 1.0.1
     */
    @Parameter
    private List<String> testsExcludes;

    /**
     * Set the plugin to be verbose during its execution.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.verbose", defaultValue = "${maven.verbose}")
    private boolean verbose = false;

    /**
     * A flag to indicate if the *.js found in <code>tests.directory</code> should be executed.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.include.javascript", defaultValue = "true")
    private boolean includeJS;

    /**
     * A flag to indicate if the *.coffee found in <code>tests.directory</code> should be executed.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.include.coffeescript", defaultValue = "true")
    private boolean includeCS;

    /**
     * Environment variables to set on the command line, instead of the default, inherited, ones.
     *
     * @since 1.0.0
     */
    @Parameter
    private Map<String, String> environmentVariables;

    /**
     * Set this to <code>true</code> to bypass unit tests entirely.
     *
     * @since 1.0.1
     */
    @Parameter(property = "casperjs.skip", defaultValue = "${maven.test.skip}")
    private boolean skip = false;

    // Parameters for the CasperJS options

    /**
     * Set the value for the CasperJS option <code>--pre=[pre-test.js]</code>: will add the tests contained in pre-test.js before executing the test
     * suite. If a <code>pre.js</code> file is found on the <code>${tests.directory}</code>, this option will be set automatically
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.pre")
    private String pre;

    /**
     * Set the value for the CasperJS option <code>--post=[post-test.js]</code>: will add the tests contained in post-test.js after having executed
     * the whole test suite. If a <code>post.js</code> file is found on the <code>${tests.directory}</code>, this option will be set automatically
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.post")
    private String post;

    /**
     * Set the value for the CasperJS option <code>--includes=[foo.js,bar.js]</code>: will includes the foo.js and bar.js files before each test file
     * execution.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.includes")
    private String includes;

    /**
     * A list of <code>&lt;includesPattern&gt;</code> elements specifying the files (by pattern) to set on the <code>--includes</code> option.<br/>
     * When not specified and the <code>${tests.directory}/includes</code> directory exists, this will be set to <br/>
     * <br/>
     * <code>&lt;includesPatterns&gt;<br/>
    &nbsp;&nbsp;&lt;includesPattern&gt;${tests.directory}/includes/**&#47;*.js&lt;/includesPattern&gt;<br/>
    &lt;/includesPatterns&gt;</code>
     *
     * @since 1.0.1
     */
    @Parameter
    private List<String> includesPatterns;

    /**
     * Should CasperJS generates XML reports, through the <code>--xunit=[filename]</code> option. If <code>true</code>, such reports will be generated
     * in the <code>reportsDirectory<code> directory,
     * with a name of <code>TEST-&lt;test filename&gt;.xml</code>.
     *
     * @since 1.0.2
     */
    @Parameter(property = "casperjs.enableXmlReports", defaultValue = "false")
    private boolean enableXmlReports;

    /**
     * Directory where the xUnit reports will be stored.
     *
     * @since 1.0.2
     */
    @Parameter(property = "casperjs.reports.directory", defaultValue = "${project.build.directory}/casperjs-reports")
    private File reportsDir;

    /**
     * Set the value for the CasperJS option <code>--log-level=[logLevel]</code>: sets the logging level (see http://casperjs.org/logging.html).
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.logLevel")
    private String logLevel;

    /**
     * Set the CasperJS --direct option: will output log messages directly to the console. Deprecated: use the <code>casperjsVerbose</code> option
     *
     * @since 1.0.0
     */
    @Deprecated
    @Parameter(property = "casperjs.direct", defaultValue = "false")
    private boolean direct;

    /**
     * For CasperJS 1.1.x, set --verbose option, --direct for CasperJS 1.0.x: will output log messages directly to the console
     *
     * @since 1.0.2
     */
    @Parameter(property = "casperjs.casperjsVerbose", defaultValue = "false")
    private boolean casperjsVerbose;

    /**
     * Set the CasperJS --fail-fast option: will terminate the current test suite as soon as a first failure is encountered.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.failFast", defaultValue = "false")
    private boolean failFast;

    /**
     * CasperJS 1.1 and above<br/>
     * Set the for the CasperJS option <code>--engine=[engine]</code>: will change the rendering engine (phantomjs or slimerjs)
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.engine")
    private String engine;

    /**
     * A list of <code>&lt;argument&gt;</code> to add to the casperjs command line.
     *
     * @since 1.0.0
     */
    @Parameter
    private List<String> arguments;

    // Injected components

    /**
     * The directory where output files will be stored
     */
    @Parameter(defaultValue = "${project.build.directory}/casperjs")
    private File targetDir;

    /**
     * The current maven session, used by the ToolChainManager
     */
    @Parameter(defaultValue = "${session}")
    private MavenSession session;

    /**
     * ToolChainManager, used to retrieve the CasperJS runtime path from user's configured toolchains
     */
    @Component
    private ToolchainManager toolchainManager;

    /**
     * The CasperJS runtime path that we will launch
     */
    private String casperRuntime;

    /**
     * The CasperJS runtime version
     */
    private DefaultArtifactVersion casperJsVersion;

    /**
     * The directory containing the scripts to include while launching tests
     */
    private File includesDir;

    /**
     * The directory containing the tests to launch
     */
    private File scriptsDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        LogUtils.setLog(getLog(), verbose);
        if (skip) {
            getLogger().info("Skipping CasperJsRunner execution");
            return;
        }
        init();
        final Collection<String> scripts = findScripts();
        final Result globalResult = executeScripts(scripts);
        getLogger().info(globalResult.print());
        afterTestExecution(globalResult);
    }

    private void init() throws MojoFailureException {
        casperRuntime = findCasperRuntime(toolchainManager, session, casperExecPath);
        if (StringUtils.isBlank(casperRuntime)) {
            throw new MojoFailureException("CasperJS executable not found");
        }

        casperJsVersion = retrieveVersion(casperRuntime, verbose);
        if (verbose) {
            getLogger().info("CasperJS version: " + casperJsVersion);
        }

        if (direct && (casperJsVersion.getMajorVersion() > 1 || casperJsVersion.getMajorVersion() == 1 && casperJsVersion.getMinorVersion() > 0)) {
            getLogger().warn("direct option is deprecated, use casperjsVerbose instead");
            casperjsVerbose = true;
        }

        testsIncludes = checkPatterns(testsIncludes, includeJS, includeCS);

        if (testsExcludes == null) {
            testsExcludes = new ArrayList<String>();
        }

        if (includesPatterns == null) {
            includesPatterns = new ArrayList<String>();
        }

        includesDir = testsDir;
        scriptsDir = testsDir;
        final File defaultIncludesDir = new File(testsDir, "includes");
        final File defaultScriptsDir = new File(testsDir, "scripts");
        if (defaultScriptsDir.exists() && defaultScriptsDir.isDirectory()) {
            getLogger().debug("'scripts' subdirectory found, altering 'scriptsDir'");
            scriptsDir = defaultScriptsDir;
            if (defaultIncludesDir.exists() && defaultIncludesDir.isDirectory() && includesPatterns.isEmpty()) {
                getLogger().debug("'includes' subdirectory found and 'includesPatterns' is empty, altering 'includesDir' and 'includesPatterns'");
                includesDir = defaultIncludesDir;
                includesPatterns.add("**/*.js");
            }
        }

        if (enableXmlReports) {
            getLogger().debug("creating directories to hold xunit file(s)");
            reportsDir.mkdirs();
        }
    }

    private Collection<String> findScripts() {
        return new OrdererScriptsFinderDecorator(new DefaultScriptsFinder(scriptsDir, test, testsIncludes, testsExcludes)).findScripts();
    }

    private Result executeScripts(final Collection<String> files) {
        final Result result = new Result();
        for (final String file : files) {
            final File f = new File(scriptsDir, file);
            getLogger().debug("Execution of test " + f.getName());
            final int res = executeScript(f);
            if (res == 0) {
                result.addSuccess();
            } else {
                getLogger().warn("Test '" + f.getName() + "' has failure");
                result.addFailure();
            }
        }
        return result;
    }

    private int executeScript(final File f) {
        final CommandLine cmdLine = new CommandLine(casperRuntime);

        // First, native options

        // Option --verbose / --direct, to output log messages to the console
        if (casperjsVerbose) {
            if (casperJsVersion.getMajorVersion() < 1 || casperJsVersion.getMajorVersion() == 1 && casperJsVersion.getMinorVersion() == 0) {
                cmdLine.addArgument("--direct");
            } else {
                cmdLine.addArgument("--verbose");
            }
        }
        // Option --log-level, to set the log level
        if (StringUtils.isNotBlank(logLevel)) {
            cmdLine.addArgument("--log-level=" + logLevel);
        }
        // Option --engine, to select phantomJS or slimerJS engine
        if (StringUtils.isNotBlank(engine)) {
            cmdLine.addArgument("--engine=" + engine);
        }

        // Then, specific ones for unit testing

        cmdLine.addArgument("test");

        // Option --includes, to includes files before each test execution
        if (StringUtils.isNotBlank(includes)) {
            cmdLine.addArgument("--includes=" + includes);
        } else if (!includesPatterns.isEmpty()) {
            final List<String> incs = new IncludesFinder(includesDir, includesPatterns).findIncludes();
            if (incs != null && !incs.isEmpty()) {
                final StringBuilder builder = new StringBuilder();
                builder.append("--includes=");
                for (final String inc : incs) {
                    builder.append(new File(includesDir, inc).getAbsolutePath());
                    builder.append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                cmdLine.addArgument(builder.toString());
            }
        }
        // Option --pre, to execute the scripts before the test suite
        if (StringUtils.isNotBlank(pre)) {
            cmdLine.addArgument("--pre=" + pre);
        } else if (new File(testsDir, "pre.js").exists()) {
            getLogger().debug("Using automatically found 'pre.js' file on " + testsDir.getName() + " directory as --pre");
            cmdLine.addArgument("--pre=" + new File(testsDir, "pre.js").getAbsolutePath());
        }
        // Option --post, to execute the scripts after the test suite
        if (StringUtils.isNotBlank(post)) {
            cmdLine.addArgument("--post=" + post);
        } else if (new File(testsDir, "post.js").exists()) {
            getLogger().debug("Using automatically found 'post.js' file on " + testsDir.getName() + " directory as --post");
            cmdLine.addArgument("--post=" + new File(testsDir, "post.js").getAbsolutePath());
        }
        // Option --xunit, to export results in XML file
        if (enableXmlReports) {
            cmdLine.addArgument("--xunit=" + new File(reportsDir, "TEST-" + buildName(scriptsDir, f) + ".xml"));
        }
        // Option --fast-fast, to terminate the test suite once a failure is
        // found
        if (failFast) {
            cmdLine.addArgument("--fail-fast");
        }
        cmdLine.addArgument(f.getAbsolutePath());
        if (arguments != null && !arguments.isEmpty()) {
            for (final String argument : arguments) {
                cmdLine.addArgument(quote(argument), false);
            }
        }
        return executeCommand(cmdLine, environmentVariables, verbose);
    }

    protected void afterTestExecution(final Result globalResult) throws MojoFailureException, MojoExecutionException {
    }

}
