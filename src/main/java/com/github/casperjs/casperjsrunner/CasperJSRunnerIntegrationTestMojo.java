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
 * @author Vilmos Nagy  <vilmos.nagy@outlook.com>
 */
@Mojo(name = "integration-test", defaultPhase = LifecyclePhase.INTEGRATION_TEST)
public class CasperJSRunnerIntegrationTestMojo extends AbstractCasperJSRunnerMojo {

    /**
     * A file in which the count of failed tests will be written.
     * We'll check this file in the verify phase to fail the build, if the testFailures ignored during the test mojo.
     */
    @Parameter(property = "casperjs.testFailure.countFile", defaultValue = "${project.build.directory}/casperjsFailureCount")
    private File testFailureCountFile;

    protected void afterTestExecution(Result globalResult) throws MojoFailureException, MojoExecutionException {
        writeFailedTestCount(globalResult);
    }

    private void writeFailedTestCount(Result globalResult) throws MojoExecutionException {
        try {
            tryToWriteFailedTestCount(globalResult);
        } catch (IOException e) {
            throw new MojoExecutionException("Could not write the failed tests' count to disk.", e);
        }
    }

    private void tryToWriteFailedTestCount(Result globalResult) throws IOException {
        createParentDirectoryIfNecessary(testFailureCountFile);
        writeFailedTestCountToExistingDir(globalResult);
    }

    private void writeFailedTestCountToExistingDir(Result globalResult) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(testFailureCountFile, false));
            bufferedWriter.write(globalResult.getFailures() + "\n");
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (Exception ignored) { }
            }
        }
    }

    private void createParentDirectoryIfNecessary(File testFailureCountFile) throws IOException {
        final File parentDir = testFailureCountFile.getParentFile();
        if (! parentDir.exists()) {
            final boolean success = parentDir.mkdirs();
            if (!success) {
                throw new IOException("Cannot create directory for output file: " + testFailureCountFile.toString());
            }
        }
    }
}
