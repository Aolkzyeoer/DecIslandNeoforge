package com.dec.decisland.datagen

import com.dec.decisland.DecIsland
import com.dec.decisland.item.ModItems
import com.dec.decisland.tag.ModItemTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagsProvider.TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper?,
) : ItemTagsProvider(output, lookupProvider, blockTags, DecIsland.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        ModItems.getItemConfigs().forEach { config ->
            val item = ModItems.getItemByConfig(config) ?: return@forEach
            config.tags.forEach { itemTag ->
                tag(itemTag).add(item)
            }
        }

        tag(ModItemTags.REPAIRS_AMETHYST_ARMOR)
            .add(Items.AMETHYST_SHARD)
            .add(Items.BUDDING_AMETHYST)
            .add(Items.AMETHYST_BLOCK)
            .add(Items.AMETHYST_CLUSTER)

        tag(ModItemTags.REPAIRS_FASHION)
            .add(Items.LEATHER)
            .add(Items.STRING)
            .add(Items.SLIME_BALL)
            .addTag(ItemTags.WOOL)
    }
}
