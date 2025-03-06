package com.capitolis.websocket.ay.debug.tool.utils;

import java.util.UUID;

public class InterpolationIdUtil {

    public static UUID tryUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
