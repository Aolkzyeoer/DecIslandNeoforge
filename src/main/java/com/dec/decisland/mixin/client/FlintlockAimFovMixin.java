package com.dec.decisland.mixin.client;

import com.dec.decisland.item.gun.GunItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class FlintlockAimFovMixin {
    // 1.21.1: GameRenderer#getFov returns double
    @Inject(method = "getFov(Lnet/minecraft/client/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    private void decisland$flintlockAimFov(Camera camera, float partialTick, boolean useFovSetting, CallbackInfoReturnable<Double> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || camera.getEntity() != mc.player || !mc.player.isCrouching()) {
            return;
        }

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) {
            stack = mc.player.getOffhandItem();
            if (!(stack.getItem() instanceof GunItem)) {
                return;
            }
        }

        cir.setReturnValue(cir.getReturnValueD() * 0.65);
    }
}
