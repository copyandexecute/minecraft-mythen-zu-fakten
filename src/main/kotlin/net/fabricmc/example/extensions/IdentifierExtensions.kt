package net.fabricmc.example.extensions

import net.minecraft.util.Identifier

fun String.toId() = Identifier("modid",this)