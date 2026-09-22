package com.dec.decisland.datagen

import com.dec.decisland.block.BlockConfig
import com.dec.decisland.block.ModBlocks
import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModItems
import com.google.gson.JsonElement
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.blockstates.BlockStateGenerator
import net.minecraft.data.models.model.ModelLocationUtils
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier

// 1.21.1 的 ModelProvider 没有 1.21.4 的子类扩展点（registerModels/getKnownItems/getKnownBlocks），
// 因此这里自建 DataProvider，按同样的方式收集并写出模型与 blockstate JSON。
class ModModelsProvider(output: PackOutput) : DataProvider {
    private val blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates")
    private val modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models")

    override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
        val blockStateGenerators = LinkedHashMap<Block, BlockStateGenerator>()
        val blockStateOutput = Consumer<BlockStateGenerator> { generator ->
            val previous = blockStateGenerators.put(generator.block, generator)
            check(previous == null) { "Duplicate blockstate definition for ${generator.block}" }
        }
        val models = LinkedHashMap<ResourceLocation, Supplier<JsonElement>>()
        val modelOutput = BiConsumer<ResourceLocation, Supplier<JsonElement>> { location, supplier ->
            val previous = models.put(location, supplier)
            check(previous == null) { "Duplicate model definition for $location" }
        }
        val blockModelGenerators = BlockModelGenerators(blockStateOutput, modelOutput) { item: Item -> }

        // 保持原有顺序：先生成物品模型，再生成方块模型。
        ModItems.ITEMS.getEntries().forEach { item ->
            val currentItem: Item = item.get()
            if (currentItem is BlockItem) return@forEach
            val config: ItemConfig = ItemConfig.getConfig(currentItem) ?: return@forEach
            // 对应 ItemModelGenerators.generateFlatItem(item, modelTemplate)（1.21.1 中为 private）。
            config.modelTemplate.create(
                ModelLocationUtils.getModelLocation(currentItem),
                TextureMapping.layer0(currentItem),
                modelOutput,
            )
        }

        ModBlocks.BLOCKS.getEntries().forEach { block ->
            val currentBlock: Block = block.get()
            val config: BlockConfig = BlockConfig.getConfig(currentBlock) ?: return@forEach
            config.blockModelGenerator.accept(blockModelGenerators)
        }

        return CompletableFuture.allOf(
            saveCollection(cachedOutput, blockStateGenerators) { block ->
                blockStatePathProvider.json(block.builtInRegistryHolder().key().location())
            },
            saveCollection(cachedOutput, models, modelPathProvider::json),
        )
    }

    private fun <T> saveCollection(
        output: CachedOutput,
        map: Map<T, Supplier<JsonElement>>,
        resolvePath: (T) -> Path,
    ): CompletableFuture<*> {
        val futures = map.map { (key, supplier) ->
            DataProvider.saveStable(output, supplier.get(), resolvePath(key))
        }
        return CompletableFuture.allOf(*futures.toTypedArray())
    }

    override fun getName(): String = "Model Definitions"
}
