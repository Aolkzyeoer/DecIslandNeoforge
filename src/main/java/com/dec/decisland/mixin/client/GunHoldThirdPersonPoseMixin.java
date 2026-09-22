package com.dec.decisland.mixin.client;
import com.dec.decisland.item.gun.GunItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(PlayerRenderer.class)
public class GunHoldThirdPersonPoseMixin {
    @Inject(
        method = "getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void decisland$gunHoldArmPose(
        AbstractClientPlayer player,
        InteractionHand hand,
        CallbackInfoReturnable<HumanoidModel.ArmPose> cir
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof GunItem) {
            cir.setReturnValue(isShortFlintlock(stack) ? HumanoidModel.ArmPose.ITEM : HumanoidModel.ArmPose.BOW_AND_ARROW);
            return;
        }
        ItemStack other = hand == InteractionHand.OFF_HAND ? player.getMainHandItem() : player.getOffhandItem();
        if (stack.isEmpty() && other.getItem() instanceof GunItem && !isShortFlintlock(other)) {
            cir.setReturnValue(HumanoidModel.ArmPose.CROSSBOW_HOLD);
        }
    }
    private static boolean isShortFlintlock(ItemStack stack) {
        try {
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            return id != null && "short_flintlock".equals(id.getPath());
        } catch (Throwable ignored) {
            return false;
        }
    }
}
