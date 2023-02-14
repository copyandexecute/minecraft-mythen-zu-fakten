package net.fabricmc.example.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface ItemBreakCallback {
    Event<ItemBreakCallback> EVENT = EventFactory.createArrayBacked(ItemBreakCallback.class, ItemBreakCallback::apply);

    private static ItemBreakCallback apply(ItemBreakCallback[] listeners) {
        return (entity, itemStack) -> {
            for (ItemBreakCallback listener : listeners) {
                listener.onItemBreak(entity, itemStack);
            }
        };
    }

    void onItemBreak(Entity entity, ItemStack itemStack);
}
