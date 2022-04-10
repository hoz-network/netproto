package net.hoz.netproto

import net.hoz.api.data.WUUID
import net.hoz.api.data.wUUID
import java.util.UUID

fun WUUID.asUuid(): UUID = UUID.fromString(this.value)

fun UUID.asProto(): WUUID = wUUID { this.toString() }