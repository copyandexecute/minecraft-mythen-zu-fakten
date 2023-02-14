package net.fabricmc.example.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.InputUtil;

public interface KeyCallback {
    Event<KeyCallback> EVENT = EventFactory.createArrayBacked(KeyCallback.class, KeyCallback::apply);

    private static KeyCallback apply(KeyCallback[] listeners) {
        return (key, action, mods) -> {
            for (KeyCallback listener : listeners) {
                listener.onKeyEvent(key, action, mods);
            }
        };
    }

    void onKeyEvent(InputUtil.Key key, int action, int mods);
}
