package com.dec.decisland.tag

import com.dec.decisland.DecIsland
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object ModBlockTags {
    private fun create(name: String): TagKey<Block> =
        TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, name))
}
