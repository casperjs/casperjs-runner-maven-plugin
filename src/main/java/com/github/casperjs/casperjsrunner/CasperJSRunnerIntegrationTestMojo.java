package com.github.casperjs.casperjsrunner;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Runs JavaScript and/or CoffeScript test files on CasperJS instance, but in "integration-test" mode. IE does not fails the build if there are test
 * errors, letting the verify Mojo doing it.
 *
 * @author Vilmos Nagy (vilmos dot nagy at outlook dot com)
 */
@Mojo(name = "integration-test", defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class CasperJSRunnerIntegrationTestMojo extends AbstractCasperJSRunnerMojo {

    /**
     * A file in which the count of failed tests will be written. We'll check this file in the verify phase to fail the build, if the testFailures
     * ignored during the test mojo.
     */
    @Parameter(property = "casperjs.testFailure.countFile", defaultValue = "${project.build.directory}/casperjsFailureCount")
    private File testFailureCountFile;

    @Override
    protected void afterTestExecution(final Result globalResult) throws MojoFailureException, MojoExecutionException {
        writeFailedTestCount(globalResult);
    }

    private void writeFailedTestCount(final Result globalResult) throws MojoExecutionException {
        try {
            tryToWriteFailedTestCount(globalResult);
        } catch (final IOException e) {
            throw new MojoExecutionException("Could not write the failed tests' count to disk.", e);
        }
    }

    private void tryToWriteFailedTestCount(final Result globalResult) throws IOException {
        createParentDirectoryIfNecessary(testFailureCountFile);
        writeFailedTestCountToExistingDir(globalResult);
    }

    private void writeFailedTestCountToExistingDir(final Result globalResult) throws IOException {
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileWriter = new FileWriter(testFailureCountFile, false);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(globalResult.getFailures() + "\n");
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (final Exception ignored) {
                    // ignore
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (final Exception ignored) {
                    // ignore
                }
            }
        }
    }

    private void createParentDirectoryIfNecessary(final File destinationFile) throws IOException {
        final File parentDir = destinationFile.getParentFile();
        if (!parentDir.exists()) {
            final boolean success = parentDir.mkdirs();
            if (!success) {
                throw new IOException("Cannot create directory for output file: " + destinationFile.toString());
            }
        }
    }
}
