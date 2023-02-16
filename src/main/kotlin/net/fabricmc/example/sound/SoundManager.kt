package net.fabricmc.example.sound

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier

object SoundManager {
    val AYYY_ZIP_ID = Identifier("modid", "bastighg")
    val HUGO_BOMB_ID = Identifier("modid", "letshugo")
    val MONTE_VERPISS_DICH_ID = Identifier("modid", "montanablack")
    val TRYMACS_MOIN_ID = Identifier("modid", "trymacs")
    val WICHTIGER_IWAS_ID = Identifier("modid", "wichtiger")
    val AYYY_ZIP_SOUND_EVENT = SoundEvent.of(AYYY_ZIP_ID)
    val HUGO_BOMB_SOUND_EVENT = SoundEvent.of(HUGO_BOMB_ID)
    val MONTE_VERPISS_DICH_SOUND_EVENT = SoundEvent.of(MONTE_VERPISS_DICH_ID)
    val TRYMACS_MOIN_SOUND_EVENT = SoundEvent.of(TRYMACS_MOIN_ID)
    val WICHTIGER_IWAS_SOUND_EVENT = SoundEvent.of(WICHTIGER_IWAS_ID)
    fun init() {
        Registry.register(Registries.SOUND_EVENT, AYYY_ZIP_ID, AYYY_ZIP_SOUND_EVENT)
        Registry.register(Registries.SOUND_EVENT, HUGO_BOMB_ID, HUGO_BOMB_SOUND_EVENT)
        Registry.register(Registries.SOUND_EVENT, MONTE_VERPISS_DICH_ID, MONTE_VERPISS_DICH_SOUND_EVENT)
        Registry.register(Registries.SOUND_EVENT, TRYMACS_MOIN_ID, TRYMACS_MOIN_SOUND_EVENT)
        Registry.register(Registries.SOUND_EVENT, WICHTIGER_IWAS_ID, WICHTIGER_IWAS_SOUND_EVENT)
    }
}
