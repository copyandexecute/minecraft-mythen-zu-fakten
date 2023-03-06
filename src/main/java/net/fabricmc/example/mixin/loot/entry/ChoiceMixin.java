package net.fabricmc.example.mixin.loot.entry;

import net.fabricmc.example.mixin.accessor.LeafEntryAccessor;
import net.fabricmc.example.myth.myths.LuckMyth;
import net.minecraft.loot.LootChoice;
import net.minecraft.loot.entry.LeafEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.loot.entry.LeafEntry$Choice")
public abstract class ChoiceMixin implements LootChoice {
    @Shadow
    @Final
    LeafEntry field_1004;

    @Override
    public int getWeight(float luck) {
        return LuckMyth.INSTANCE.apply(((LeafEntryAccessor) field_1004), luck);
    }
}
