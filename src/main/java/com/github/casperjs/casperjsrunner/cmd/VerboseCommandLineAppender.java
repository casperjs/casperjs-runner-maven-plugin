package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;

public class VerboseCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (parameters.isCasperjsVerbose()) {
            if (parameters.getCasperJsVersion().getMajorVersion() < 1
                    || parameters.getCasperJsVersion().getMajorVersion() == 1 && parameters.getCasperJsVersion().getMinorVersion() == 0) {
                cmdLine.addArgument("--direct");
            } else {
                cmdLine.addArgument("--verbose");
            }
        }
    }

}
