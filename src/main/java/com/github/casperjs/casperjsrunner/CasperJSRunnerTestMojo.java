package com.github.casperjs.casperjsrunner;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author Vilmos Nagy  <vilmos.nagy@outlook.com>
 */
@Mojo(name = "test", defaultPhase = LifecyclePhase.TEST, threadSafe = true)
public class CasperJSRunnerTestMojo extends AbstractCasperJSRunnerMojo {

    /**
     * Do we ignore the tests failures. If yes, the plugin will not fail at the end if there was tests failures.
     *
     * @since 1.0.0
     */
    @Parameter(property = "casperjs.ignoreTestFailures", defaultValue = "${maven.test.failure.ignore}")
    private boolean ignoreTestFailures = false;

    @Override
    protected void afterTestExecution(Result globalResult) throws MojoFailureException {
        if (!ignoreTestFailures && globalResult.getFailures() > 0) {
            throw new MojoFailureException("There are " + globalResult.getFailures() + " tests failures");
        }
    }
}
