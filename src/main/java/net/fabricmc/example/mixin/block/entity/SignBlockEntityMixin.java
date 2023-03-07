package net.fabricmc.example.mixin.block.entity;

import net.fabricmc.example.myth.myths.IRainbow;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntity implements IRainbow {
    @Shadow protected abstract void updateListeners();

    private boolean isRainbow;

    public SignBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void writeNbtInjection(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Rainbow", this.isRainbow);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void readNbtInjection(NbtCompound nbt, CallbackInfo ci) {
        this.isRainbow = nbt.getBoolean("Rainbow");
    }

    @Override
    public boolean isRainbow() {
        return isRainbow;
    }

    @Override
    public void setRainbow(boolean flag) {
        if (this.isRainbow != flag) {
            this.isRainbow = flag;
            this.updateListeners();
        }
    }
}
