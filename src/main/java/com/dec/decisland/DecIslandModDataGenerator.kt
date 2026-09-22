package com.dec.decisland

import com.dec.decisland.datagen.ModBlockLootTablesProvider
import com.dec.decisland.datagen.ModBlockTagsProvider
import com.dec.decisland.datagen.ModDataMapProvider
import com.dec.decisland.datagen.ModItemTagsProvider
import com.dec.decisland.datagen.ModLangProvider
import com.dec.decisland.datagen.ModModelsProvider
import com.dec.decisland.datagen.ModRecipeProvider
import com.dec.decisland.datagen.ModRegistryListProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = DecIsland.MOD_ID)
object DecIslandModDataGenerator {
    // 1.21.1 的 GatherDataEvent 没有 Client/Server 子类，客户端/服务端由 includeClient()/includeServer() 区分。
    @SubscribeEvent
    @JvmStatic
    fun gatherData(event: GatherDataEvent) {
        event.createProvider(::ModModelsProvider)
        event.createProvider(::ModDataMapProvider)
        event.createProvider(::ModRecipeProvider)
        event.createProvider(::ModRegistryListProvider)

        arrayOf("en_us", "zh_cn").forEach { locale ->
            event.createProvider { output ->
                object : ModLangProvider(output, locale) {}
            }
        }

        event.createProvider { output, lookupProvider ->
            LootTableProvider(
                output,
                setOf(),
                listOf(LootTableProvider.SubProviderEntry(::ModBlockLootTablesProvider, LootContextParamSets.BLOCK)),
                lookupProvider,
            )
        }

        // 1.21.1 的 TagsProvider 构造需要 ExistingFileHelper；ItemTagsProvider 还需要方块标签的 TagLookup。
        val blockTags = event.createProvider { output, lookupProvider ->
            ModBlockTagsProvider(output, lookupProvider, event.existingFileHelper)
        }
        event.createProvider { output, lookupProvider ->
            ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), event.existingFileHelper)
        }
    }
}
