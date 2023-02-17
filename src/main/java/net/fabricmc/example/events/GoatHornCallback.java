package net.fabricmc.example.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface GoatHornCallback {
    Event<GoatHornCallback> EVENT = EventFactory.createArrayBacked(GoatHornCallback.class, GoatHornCallback::apply);

    private static GoatHornCallback apply(GoatHornCallback[] listeners) {
        return (world, player, instrument) -> {
            for (GoatHornCallback listener : listeners) {
                listener.onGoating(world, player, instrument);
            }
        };
    }

    void onGoating(World world, PlayerEntity player, Instrument instrument);
}
