package com.github.casperjs.casperjsrunner;

import static java.lang.System.getProperty;

public class OSUtils {

    public static boolean isWindows() {
        return getProperty("os.name").toLowerCase().indexOf("win") >= 0;
    }
}
