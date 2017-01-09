package com.github.casperjs.casperjsrunner.cmd;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.StringUtils;

import com.github.casperjs.casperjsrunner.IncludesFinder;

import java.io.File;
import java.util.List;

public class IncludesCommandLineAppender implements CommandLineAppender {

    @Override
    public void append(final CommandLine cmdLine, final Parameters parameters) {
        if (StringUtils.isNotBlank(parameters.getIncludes())) {
            cmdLine.addArgument("--includes=" + parameters.getIncludes());
        } else if (parameters.getIncludesPatterns() != null && !parameters.getIncludesPatterns().isEmpty()) {
            final List<String> incs = new IncludesFinder(parameters.getIncludesDir(), parameters.getIncludesPatterns()).findIncludes();
            if (incs != null && !incs.isEmpty()) {
                final StringBuilder builder = new StringBuilder();
                builder.append("--includes=");
                for (final String inc : incs) {
                    builder.append(new File(parameters.getIncludesDir(), inc).getAbsolutePath());
                    builder.append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
                cmdLine.addArgument(builder.toString());
            }
        }
    }

}
