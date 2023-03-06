package net.fabricmc.example.myth

abstract class Myth(val name: String) {
    var isActive: Boolean = false
    fun toggle() {
        isActive = !isActive
    }
}