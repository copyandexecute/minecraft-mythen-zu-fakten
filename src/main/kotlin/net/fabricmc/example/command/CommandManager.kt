package net.fabricmc.example.command

import net.fabricmc.example.myth.myths
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.text.literalText

object CommandManager {
    fun init() {
        command("myth") {
            myths.forEach {
                literal(it.name.lowercase()) {
                    runs {
                        it.toggle()
                        this.source.sendMessage(literalText("${it.name} is now ${it.isActive}"))
                    }
                }
            }
        }
    }
}