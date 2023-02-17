package net.fabricmc.example.render.entity

object EnderDragonEntityRendererModifier {
    var killedDragons = 0
    fun increaseKilledDragons(amount: Int) {
        this.killedDragons += amount
    }
}
