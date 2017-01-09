package com.github.casperjs.casperjsrunner.cmd;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class PreCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (StringUtils.isNotBlank(parameters.getPre())) {
            cmdLine.addArgument("--pre=" + parameters.getPre());
        } else if (new File(parameters.getTestsDir(), "pre.js").exists()) {
            getLogger().debug("Using automatically found 'pre.js' file on " + parameters.getTestsDir().getName() + " directory as --pre");
            cmdLine.addArgument("--pre=" + new File(parameters.getTestsDir(), "pre.js").getAbsolutePath());
        }
    }

}
