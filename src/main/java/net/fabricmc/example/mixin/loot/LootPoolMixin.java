package net.fabricmc.example.mixin.loot;

import com.google.common.collect.Lists;
import net.fabricmc.example.myth.myths.LuckMyth;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootChoice;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Mixin(LootPool.class)
public abstract class LootPoolMixin {
    @Shadow @Final public LootPoolEntry[] entries;

    @Redirect(method = "addGeneratedLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/provider/number/LootNumberProvider;nextFloat(Lnet/minecraft/loot/context/LootContext;)F"))
    private float luckMyth(LootNumberProvider instance, LootContext lootContext) {
        return LuckMyth.INSTANCE.apply(instance, lootContext);
    }

    /**
     * @author NoRiskk
     * @reason weil ichs kann
     */
    @Overwrite
    private void supplyOnce(Consumer<ItemStack> lootConsumer, LootContext context) {
        Random random = context.getRandom();
        List<LootChoice> list = Lists.newArrayList();
        MutableInt mutableInt = new MutableInt();
        LootPoolEntry[] var6 = this.entries;
        int j = var6.length;

        for(int var8 = 0; var8 < j; ++var8) {
            LootPoolEntry lootPoolEntry = var6[var8];
            lootPoolEntry.expand(context, (choice) -> {
                int i = choice.getWeight(context.getLuck());
                System.out.println("Weight: " + i);
                if (i > 0) {
                    list.add(choice);
                    mutableInt.add(i);
                }

            });
        }

        int i = list.size();
        if (mutableInt.intValue() != 0 && i != 0) {
            if (i == 1) {
                ((LootChoice)list.get(0)).generateLoot(lootConsumer, context);
            } else {
                j = random.nextInt(mutableInt.intValue());
                Iterator var11 = list.iterator();

                LootChoice lootChoice;
                do {
                    if (!var11.hasNext()) {
                        return;
                    }
                    lootChoice = (LootChoice)var11.next();
                    j -= lootChoice.getWeight(context.getLuck());
                } while(j >= 0);

                lootChoice.generateLoot(lootConsumer, context);
            }
        }
    }
}
