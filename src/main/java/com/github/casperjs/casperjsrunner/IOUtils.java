package com.github.casperjs.casperjsrunner;

import static com.github.casperjs.casperjsrunner.LogUtils.getLogger;

import java.io.Closeable;

public class IOUtils {

    private IOUtils() {
        // only used as static
    }

    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception ex) {
                getLogger().debug("Exception occured while closing resource " + closeable + ": " + ex);
            }
        }
    }
}
