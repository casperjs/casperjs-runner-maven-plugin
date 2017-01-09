package com.github.casperjs.casperjsrunner;

import org.apache.maven.plugin.logging.Log;

public class LogUtils {

    private static Logger logger;

    private LogUtils() {
        // only used as static
    }

    public static void setLog(final Log log, final boolean verbose) {
        logger = new Logger(log, verbose);
    }

    public static Logger getLogger() {
        return logger;
    }

}