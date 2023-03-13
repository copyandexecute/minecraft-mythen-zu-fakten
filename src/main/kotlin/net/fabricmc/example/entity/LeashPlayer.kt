package net.fabricmc.example.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import net.fabricmc.example.extensions.toId
import net.fabricmc.example.serialization.UUIDSerializer
import net.minecraft.nbt.NbtList
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.silkmc.silk.network.packet.s2cPacket
import java.util.UUID

interface LeashPlayer {
    val entities: MutableSet<UUID>
    fun handleUuid(uuid: UUID, attached: Boolean)
}

@OptIn(ExperimentalSerializationApi::class)
object LeashPlayerModifier {
    @Serializable
    data class Data(
        @Serializable(with = UUIDSerializer::class)
        val entity: UUID,
        val attached: Boolean
    )

    private val leashPacket = s2cPacket<Data>("leash".toId())
    fun init() {
        leashPacket.receiveOnClient { packet, context ->
            (context.client.player as LeashPlayer).handleUuid(packet.entity, packet.attached)
        }
    }
    fun handleLeash(player: ServerPlayerEntity, entity: UUID, attached: Boolean) {
        (player as LeashPlayer).handleUuid(entity, attached)
        leashPacket.send(Data(entity, attached), player)
    }
}
