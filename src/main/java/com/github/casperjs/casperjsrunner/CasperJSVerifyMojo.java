package com.github.casperjs.casperjsrunner;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Verifies that there was no test failure during the integration-test Mojo.
 *
 * @author Vilmos Nagy (vilmos dot nagy at outlook dot com)
 */
@Mojo(name = "verify", defaultPhase = LifecyclePhase.VERIFY)
public class CasperJSVerifyMojo extends AbstractMojo {

    /**
     * A file in which the count of failed tests will be written. We'll check this file in the verify phase to fail the build, if the testFailures
     * ignored during the test mojo.
     */
    @Parameter(property = "casperjs.testFailure.countFile", defaultValue = "${project.build.directory}/casperjsFailureCount")
    private File testFailureCountFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!testFailureCountFile.exists()) {
            throw new MojoFailureException(
                    "The testFailureCountFile " + testFailureCountFile.getAbsolutePath() + " doesn't exists. Run tests before the verify phase!");
        }

        final String firstLine = readFirstLineOfFile();
        final Integer failedTestCount = Integer.valueOf(firstLine);
        if (failedTestCount > 0) {
            throw new MojoFailureException("Integration test verification error: There are " + failedTestCount + " tests failures");
        }
    }

    private String readFirstLineOfFile() throws MojoExecutionException {
        try {
            return tryToReadFirstLineOfFile();
        } catch (final IOException e) {
            throw new MojoExecutionException("", e);
        }
    }

    private String tryToReadFirstLineOfFile() throws IOException {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(testFailureCountFile);
            bufferedReader = new BufferedReader(fileReader);
            return bufferedReader.readLine();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final Exception ignored) {
                    // ignore
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (final Exception ignored) {
                    // ignore
                }
            }
        }
    }

}
