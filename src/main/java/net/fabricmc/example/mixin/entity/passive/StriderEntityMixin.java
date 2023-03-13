package net.fabricmc.example.mixin.entity.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.Saddleable;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity implements ItemSteerable, Saddleable {
    protected StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

   /* @Nullable
    @Override
    public Entity getPrimaryPassenger() {
        Entity entity = this.getFirstPassenger();
        for (Entity passenger : this.getPassengersDeep()) {
            if (passenger.isPlayer()) {
                entity = passenger;
                break;
            }
        }
        return entity != null && this.canEntityControl(entity) ? entity : null;
    } */
}
