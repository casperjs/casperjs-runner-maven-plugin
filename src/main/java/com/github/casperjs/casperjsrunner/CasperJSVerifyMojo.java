package com.github.casperjs.casperjsrunner;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;

/**
 * Verifies that there was no test failure during the test Mojo.
 *
 * @author Vilmos Nagy
 */
@Mojo(name = "verify")
public class CasperJSVerifyMojo extends AbstractMojo {

    /**
     * A file in which the count of failed tests will be written.
     * We'll check this file in the verify phase to fail the build, if the testFailures ignored during the test mojo.
     */
    @Parameter(property = "casperjs.testFailure.countFile", defaultValue = "${project.build.directory}/casperjsFailureCount")
    private File testFailureCountFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!testFailureCountFile.exists()) {
            throw new MojoFailureException("The testFailureCountFile " + testFailureCountFile.getAbsolutePath() + " doesn't exists. Run tests before the verify phase!");
        }

        String firstLine = readFirstLineOfFile();
        Integer failedTestCount = Integer.valueOf(firstLine);
        if (failedTestCount > 0) {
            throw new MojoFailureException("There are " + failedTestCount + " tests failures");
        }
    }

    private String readFirstLineOfFile() throws MojoExecutionException {
        try {
            return tryToReadFirstLineOfFile();
        } catch (IOException e) {
            throw new MojoExecutionException("", e);
        }
    }

    private String tryToReadFirstLineOfFile() throws IOException {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(testFailureCountFile));
            return bufferedReader.readLine();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {}
            }
        }
    }

}
