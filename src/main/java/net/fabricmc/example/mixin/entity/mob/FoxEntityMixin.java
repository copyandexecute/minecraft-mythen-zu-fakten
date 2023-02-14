package net.fabricmc.example.mixin.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends AnimalEntity {
    protected FoxEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    /*
    @Override
    public void onLanding() {
        super.onLanding();
        world.setBlockState(this.getBlockPos().up(1), Blocks.WATER.getDefaultState());
    }*/
}
