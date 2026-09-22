package com.dec.decisland.client.renderer

import com.dec.decisland.DecIsland
import com.dec.decisland.client.model.EmptyModel
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

class EmptyRenderer(context: EntityRendererProvider.Context) : EntityRenderer<Entity>(context) {
    private val model = EmptyModel(context.bakeLayer(EmptyModel.LAYER_LOCATION))

    override fun getTextureLocation(entity: Entity): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "textures/empty.png")
}
