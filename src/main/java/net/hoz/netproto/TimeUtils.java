package net.hoz.netproto;

import com.google.protobuf.Timestamp;
import lombok.experimental.UtilityClass;

import java.time.Instant;

/**
 * Utility class for converting {@link Instant} into {@link Timestamp} and vice-versa.
 */
@UtilityClass
public class TimeUtils {

    public Instant convert(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    public Timestamp convert(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}
