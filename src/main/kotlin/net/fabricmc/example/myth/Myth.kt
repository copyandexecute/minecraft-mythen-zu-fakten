package net.fabricmc.example.myth

import net.minecraft.server.command.ServerCommandSource
import net.silkmc.silk.commands.LiteralCommandBuilder

abstract class Myth(val name: String) {
    var isActive: Boolean = false

    constructor(name: String, isActive: Boolean) : this(name) {
        this.isActive = isActive
    }

    fun toggle() {
        isActive = !isActive
    }

    open fun appendCommand(commandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
    }
}
