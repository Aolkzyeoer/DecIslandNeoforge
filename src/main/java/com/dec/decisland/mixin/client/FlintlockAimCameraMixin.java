package com.dec.decisland.mixin.client;

import com.dec.decisland.client.RecoilClient;
import com.dec.decisland.item.gun.GunItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class FlintlockAimCameraMixin {
    @Shadow
    public abstract Vec3 getPosition();

    @Shadow
    protected abstract void setPosition(Vec3 vec3);

    // 1.21.1: Camera#setup takes a BlockGetter (Level came in later versions)
    @Inject(method = "setup(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/world/entity/Entity;ZZF)V", at = @At("TAIL"))
    private void decisland$flintlockAimCamera(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || entity != mc.player || detached || !mc.player.isCrouching()) {
            return;
        }

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) {
            stack = mc.player.getOffhandItem();
            if (!(stack.getItem() instanceof GunItem)) {
                return;
            }
        }

        Vec3 look = mc.player.getLookAngle().normalize();
        Vec3 aimOffset = look.scale(0.25);
        double back = RecoilClient.getCameraBackOffset(partialTick);
        Vec3 recoilOffset = look.scale(-back);
        this.setPosition(this.getPosition().add(aimOffset).add(recoilOffset));
    }
}
