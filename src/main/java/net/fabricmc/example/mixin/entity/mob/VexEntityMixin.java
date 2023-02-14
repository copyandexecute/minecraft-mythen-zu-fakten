package net.fabricmc.example.mixin.entity.mob;

import net.fabricmc.example.entity.VexEntityModifier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VexEntity.class)
public abstract class VexEntityMixin extends HostileEntity implements Ownable {
    protected VexEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }
    private boolean isTransforming;

    @Override
    public ActionResult interactAt(PlayerEntity playerEntity, Vec3d vec3d, Hand hand) {
        if (isTransforming) return super.interactAt(playerEntity, vec3d, hand);
        ItemStack beetroot = null;
        for (ItemStack handItem : playerEntity.getHandItems()) {
            if (handItem.isOf(Items.BEETROOT)) {
                beetroot = handItem;
                break;
            }
        }

        if (beetroot != null) {
            this.isTransforming = true;
            VexEntityModifier.INSTANCE.transform((VexEntity) (Object) this, beetroot);
            return ActionResult.PASS;
        }
        return super.interactAt(playerEntity, vec3d, hand);
    }

    //just used for testing
   /* @Redirect(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/VexEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"))
    private void injected(VexEntity instance, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        ItemStack stack = new ItemStack(Items.IRON_SWORD);
        stack.setDamage(249);
        this.equipStack(EquipmentSlot.MAINHAND, stack);
    } */
}
