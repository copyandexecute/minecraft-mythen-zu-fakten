package net.fabricmc.example.entity

interface RocketSpammer {
    fun addRocket()
    fun getUsedRockets(): Int
    fun getLastUsedRocket(): Long
    fun resetRockets()
}