package net.fabricmc.example.mixin.accessor;

import net.minecraft.loot.entry.LeafEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LeafEntry.class)
public interface LeafEntryAccessor {
    @Accessor
    int getWeight();
    @Accessor("weight")
    void setWeight(int weight);
    @Accessor
    int getQuality();
    @Accessor("quality")
    void setQuality(int quality);
}
