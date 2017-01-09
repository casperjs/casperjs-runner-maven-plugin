package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;

import java.io.File;

public class XUnitCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (parameters.isEnableXmlReports()) {
            cmdLine.addArgument("--xunit=" + new File(parameters.getReportsDir(), "TEST-" + parameters.getScriptName() + ".xml"));
        }
    }

}
