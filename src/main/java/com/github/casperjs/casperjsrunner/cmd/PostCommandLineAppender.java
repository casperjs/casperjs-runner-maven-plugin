package com.github.casperjs.casperjsrunner.cmd;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class PostCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (StringUtils.isNotBlank(parameters.getPost())) {
            cmdLine.addArgument("--post=" + parameters.getPost());
        } else if (new File(parameters.getTestsDir(), "post.js").exists()) {
            getLogger().debug("Using automatically found 'post.js' file on " + parameters.getTestsDir().getName() + " directory as --post");
            cmdLine.addArgument("--post=" + new File(parameters.getTestsDir(), "post.js").getAbsolutePath());
        }
    }

}
