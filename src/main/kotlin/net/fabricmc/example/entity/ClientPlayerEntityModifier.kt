package net.fabricmc.example.entity

import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.network.message.ArgumentSignatureDataMap
import net.minecraft.network.message.LastSeenMessageList
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket
import java.time.Instant
import java.util.*

object ClientPlayerEntityModifier {
    fun ClientPlayerEntity.sendCommand(name: String) {
        networkHandler?.sendPacket(
            CommandExecutionC2SPacket(
                name,
                Instant.now(),
                0L,
                ArgumentSignatureDataMap.EMPTY,
                LastSeenMessageList.Acknowledgment(0, BitSet())
            )
        )
    }
}
