package pl.jg.iam.common;

import java.util.UUID;

public class IdAppGenerator {

    /**
     * Generate a new UUID string
     *
     * @return UUID string
     */

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
