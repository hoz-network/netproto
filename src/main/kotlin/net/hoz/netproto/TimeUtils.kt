package net.hoz.netproto

import com.google.protobuf.Timestamp
import java.time.Instant

fun Instant.asProto() = Timestamp.newBuilder()
    .setSeconds(this.epochSecond)
    .setNanos(this.nano)
    .build()

fun Timestamp.asInstant() = Instant.ofEpochSecond(this.seconds, this.nanos.toLong())