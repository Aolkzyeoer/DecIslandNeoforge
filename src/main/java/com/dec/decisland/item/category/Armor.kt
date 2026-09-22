package com.dec.decisland.item.category

import com.dec.decisland.item.ItemConfig
import com.dec.decisland.item.ModArmorMaterials
import com.dec.decisland.item.ModArmorMaterials.ModArmorMaterial
import com.dec.decisland.item.ModCreativeModeTabs
import com.dec.decisland.item.ModItems
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Supplier

object Armor {
    @JvmField
    val creativeTab: Supplier<CreativeModeTab> = ModCreativeModeTabs.DECISLAND_WEAPONS_TAB

    private fun registerArmorPiece(
        name: String,
        material: ModArmorMaterial,
        type: ArmorItem.Type,
        repairItem: Supplier<Item>? = null,
        repairTag: TagKey<Item>? = null,
    ): DeferredItem<Item> =
        ModItems.registerItem(
            ItemConfig.Builder(name)
                .func { props ->
                    ArmorItem(
                        material.holder,
                        type,
                        props.durability(type.getDurability(material.durabilityMultiplier)),
                    )
                }
                .creativeTab(creativeTab)
                .build(),
        )

    @JvmField
    val AMETHYST_HELMET: DeferredItem<Item> =
        registerArmorPiece("amethyst_helmet", ModArmorMaterials.AMETHYST, ArmorItem.Type.HELMET)

    @JvmField
    val AMETHYST_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("amethyst_chestplate", ModArmorMaterials.AMETHYST, ArmorItem.Type.CHESTPLATE)

    @JvmField
    val AMETHYST_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("amethyst_leggings", ModArmorMaterials.AMETHYST, ArmorItem.Type.LEGGINGS)

    @JvmField
    val AMETHYST_BOOTS: DeferredItem<Item> =
        registerArmorPiece("amethyst_boots", ModArmorMaterials.AMETHYST, ArmorItem.Type.BOOTS)

    @JvmField
    val CRYING_HELMET: DeferredItem<Item> =
        registerArmorPiece("crying_helmet", ModArmorMaterials.CRYING, ArmorItem.Type.HELMET, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val CRYING_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("crying_chestplate", ModArmorMaterials.CRYING, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val CRYING_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("crying_leggings", ModArmorMaterials.CRYING, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val CRYING_BOOTS: DeferredItem<Item> =
        registerArmorPiece("crying_boots", ModArmorMaterials.CRYING, ArmorItem.Type.BOOTS, repairItem = Supplier { Items.CRYING_OBSIDIAN })

    @JvmField
    val DIRT_HELMET: DeferredItem<Item> =
        registerArmorPiece("dirt_helmet", ModArmorMaterials.DIRT, ArmorItem.Type.HELMET, repairItem = Supplier { Items.DIRT })

    @JvmField
    val DIRT_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("dirt_chestplate", ModArmorMaterials.DIRT, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Items.DIRT })

    @JvmField
    val DIRT_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("dirt_leggings", ModArmorMaterials.DIRT, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Items.DIRT })

    @JvmField
    val DIRT_BOOTS: DeferredItem<Item> =
        registerArmorPiece("dirt_boots", ModArmorMaterials.DIRT, ArmorItem.Type.BOOTS, repairItem = Supplier { Items.DIRT })

    @JvmField
    val EMERALD_HELMET: DeferredItem<Item> =
        registerArmorPiece("emerald_helmet", ModArmorMaterials.EMERALD, ArmorItem.Type.HELMET, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EMERALD_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("emerald_chestplate", ModArmorMaterials.EMERALD, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EMERALD_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("emerald_leggings", ModArmorMaterials.EMERALD, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EMERALD_BOOTS: DeferredItem<Item> =
        registerArmorPiece("emerald_boots", ModArmorMaterials.EMERALD, ArmorItem.Type.BOOTS, repairItem = Supplier { Items.EMERALD })

    @JvmField
    val EVERLASTING_WINTER_HELMET: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_helmet", ModArmorMaterials.EVERLASTING_WINTER, ArmorItem.Type.HELMET, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val EVERLASTING_WINTER_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_chestplate", ModArmorMaterials.EVERLASTING_WINTER, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val EVERLASTING_WINTER_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_leggings", ModArmorMaterials.EVERLASTING_WINTER, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val EVERLASTING_WINTER_BOOTS: DeferredItem<Item> =
        registerArmorPiece("everlasting_winter_boots", ModArmorMaterials.EVERLASTING_WINTER, ArmorItem.Type.BOOTS, repairItem = Supplier { Material.ICE_INGOT.get() })

    @JvmField
    val FROZEN_HELMET: DeferredItem<Item> =
        registerArmorPiece("frozen_helmet", ModArmorMaterials.FROZEN, ArmorItem.Type.HELMET)

    @JvmField
    val FROZEN_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("frozen_chestplate", ModArmorMaterials.FROZEN, ArmorItem.Type.CHESTPLATE)

    @JvmField
    val FROZEN_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("frozen_leggings", ModArmorMaterials.FROZEN, ArmorItem.Type.LEGGINGS)

    @JvmField
    val FROZEN_BOOTS: DeferredItem<Item> =
        registerArmorPiece("frozen_boots", ModArmorMaterials.FROZEN, ArmorItem.Type.BOOTS)

    @JvmField
    val LAVA_HELMET: DeferredItem<Item> =
        registerArmorPiece("lava_helmet", ModArmorMaterials.LAVA, ArmorItem.Type.HELMET, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val LAVA_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("lava_chestplate", ModArmorMaterials.LAVA, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val LAVA_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("lava_leggings", ModArmorMaterials.LAVA, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val LAVA_BOOTS: DeferredItem<Item> =
        registerArmorPiece("lava_boots", ModArmorMaterials.LAVA, ArmorItem.Type.BOOTS, repairItem = Supplier { Material.LAVA_INGOT.get() })

    @JvmField
    val RUPERT_HELMET: DeferredItem<Item> =
        registerArmorPiece("rupert_helmet", ModArmorMaterials.RUPERT, ArmorItem.Type.HELMET, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val RUPERT_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("rupert_chestplate", ModArmorMaterials.RUPERT, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val RUPERT_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("rupert_leggings", ModArmorMaterials.RUPERT, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val RUPERT_BOOTS: DeferredItem<Item> =
        registerArmorPiece("rupert_boots", ModArmorMaterials.RUPERT, ArmorItem.Type.BOOTS, repairItem = Supplier { Items.GHAST_TEAR })

    @JvmField
    val SHULKER_HELMET: DeferredItem<Item> =
        registerArmorPiece("shulker_helmet", ModArmorMaterials.SHULKER, ArmorItem.Type.HELMET, repairItem = Supplier { Items.SHULKER_SHELL })

    @JvmField
    val STEEL_HELMET: DeferredItem<Item> =
        registerArmorPiece("steel_helmet", ModArmorMaterials.STEEL, ArmorItem.Type.HELMET, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STEEL_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("steel_chestplate", ModArmorMaterials.STEEL, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STEEL_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("steel_leggings", ModArmorMaterials.STEEL, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STEEL_BOOTS: DeferredItem<Item> =
        registerArmorPiece("steel_boots", ModArmorMaterials.STEEL, ArmorItem.Type.BOOTS, repairItem = Supplier { Material.STEEL_INGOT.get() })

    @JvmField
    val STONE_HELMET: DeferredItem<Item> =
        registerArmorPiece("stone_helmet", ModArmorMaterials.STONE, ArmorItem.Type.HELMET, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val STONE_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("stone_chestplate", ModArmorMaterials.STONE, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val STONE_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("stone_leggings", ModArmorMaterials.STONE, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val STONE_BOOTS: DeferredItem<Item> =
        registerArmorPiece("stone_boots", ModArmorMaterials.STONE, ArmorItem.Type.BOOTS, repairItem = Supplier { Items.COBBLESTONE })

    @JvmField
    val TURTLE_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("turtle_chestplate", ModArmorMaterials.TURTLE, ArmorItem.Type.CHESTPLATE, repairItem = Supplier { Items.TURTLE_SCUTE })

    @JvmField
    val TURTLE_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("turtle_leggings", ModArmorMaterials.TURTLE, ArmorItem.Type.LEGGINGS, repairItem = Supplier { Items.TURTLE_SCUTE })

    @JvmField
    val TURTLE_BOOTS: DeferredItem<Item> =
        registerArmorPiece("turtle_boots", ModArmorMaterials.TURTLE, ArmorItem.Type.BOOTS, repairItem = Supplier { Items.TURTLE_SCUTE })

    @JvmField
    val WOOD_HELMET: DeferredItem<Item> =
        registerArmorPiece("wood_helmet", ModArmorMaterials.WOOD, ArmorItem.Type.HELMET, repairTag = ItemTags.PLANKS)

    @JvmField
    val WOOD_CHESTPLATE: DeferredItem<Item> =
        registerArmorPiece("wood_chestplate", ModArmorMaterials.WOOD, ArmorItem.Type.CHESTPLATE, repairTag = ItemTags.PLANKS)

    @JvmField
    val WOOD_LEGGINGS: DeferredItem<Item> =
        registerArmorPiece("wood_leggings", ModArmorMaterials.WOOD, ArmorItem.Type.LEGGINGS, repairTag = ItemTags.PLANKS)

    @JvmField
    val WOOD_BOOTS: DeferredItem<Item> =
        registerArmorPiece("wood_boots", ModArmorMaterials.WOOD, ArmorItem.Type.BOOTS, repairTag = ItemTags.PLANKS)

    @JvmStatic
    fun load() {
    }
}
