package com.dec.decisland.client.renderer

import com.dec.decisland.entity.projectile.dart.DartEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext

class DartRenderer(context: EntityRendererProvider.Context) : EntityRenderer<DartEntity>(context) {
    private val itemRenderer = context.itemRenderer

    init {
        shadowRadius = 0.0f
        shadowStrength = 0.0f
    }

    override fun getTextureLocation(entity: DartEntity): ResourceLocation = TextureAtlas.LOCATION_BLOCKS

    override fun render(
        entity: DartEntity,
        entityYaw: Float,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
    ) {
        poseStack.pushPose()
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp(partialTick, entity.yRotO, entity.yRot) - 90.0f))
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.randomTilt))
        poseStack.mulPose(
            Axis.ZP.rotationDegrees(
                Mth.rotLerp(partialTick, entity.xRotO, entity.xRot) -
                    Mth.rotLerp(partialTick, entity.spinRotationO, entity.spinRotation),
            ),
        )
        itemRenderer.renderStatic(
            entity.item,
            ItemDisplayContext.FIXED,
            packedLight,
            OverlayTexture.NO_OVERLAY,
            poseStack,
            bufferSource,
            entity.level(),
            entity.id,
        )
        poseStack.popPose()
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)
    }
}
