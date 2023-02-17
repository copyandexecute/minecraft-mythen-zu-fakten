package net.fabricmc.example.mixin.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.world.entity.EntityLike;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, EntityLike, CommandOutput {
    @Shadow
    private ImmutableList<Entity> passengerList;

    @Shadow
    public abstract Iterable<Entity> getPassengersDeep();

    /**
     * @author NoRiskk
     * @reason Weil ichs kann
     */
    @Overwrite
    @Nullable
    public Entity getFirstPassenger() {
        List<Entity> deepList = StreamSupport.stream(this.getPassengersDeep().spliterator(), false).toList();
        if (deepList.stream().anyMatch(Entity::isPlayer)) {
            return deepList.stream().filter(Entity::isPlayer).findFirst().orElse(null);
        } else {
            return this.passengerList.isEmpty() ? null : this.passengerList.get(0);
        }
    }
}
