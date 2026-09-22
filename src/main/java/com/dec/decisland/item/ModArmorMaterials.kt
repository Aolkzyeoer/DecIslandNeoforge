package com.dec.decisland.item

import com.dec.decisland.tag.ModItemTags
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import java.util.function.Supplier

/**
 * 1.21.1 兼容层：以 1.21.4+ ArmorMaterial 的参数形态创建 1.21.1 的
 * [ArmorMaterial] 记录（耐久作为倍率保存在 [ModArmorMaterial] 中，注册物品时使用）。
 */
object ModArmorMaterials {
    private val EMPTY_REPAIR_TAG: TagKey<Item> =
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("decisland", "unused_repair"))

    class ModArmorMaterial(val holder: Holder<ArmorMaterial>, val durabilityMultiplier: Int)

    private fun createArmorMaterial(
        durability: Int,
        boots: Int,
        leggings: Int,
        chestplate: Int,
        helmet: Int,
        enchantability: Int,
        equipSound: Holder<SoundEvent>,
        toughness: Float,
        knockbackResistance: Float,
        repairTag: TagKey<Item>,
        asset: ResourceLocation,
    ): ModArmorMaterial {
        val material = ArmorMaterial(
            makeDefense(boots, leggings, chestplate, helmet, chestplate),
            enchantability,
            equipSound,
            Supplier { Ingredient.of(repairTag) },
            listOf(ArmorMaterial.Layer(asset)),
            toughness,
            knockbackResistance,
        )
        return ModArmorMaterial(Holder.direct(material), durability)
    }

    @JvmField
    val FASHION: ModArmorMaterial = createArmorMaterial(
        durability = 4,
        boots = 1,
        leggings = 1,
        chestplate = 1,
        helmet = 1,
        enchantability = 10,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = ModItemTags.REPAIRS_FASHION,
        asset = ModEquipmentAssets.FASHION,
    )

    @JvmField
    val AMETHYST: ModArmorMaterial = createArmorMaterial(
        durability = 12,
        boots = 2,
        leggings = 2,
        chestplate = 5,
        helmet = 2,
        enchantability = 30,
        equipSound = SoundEvents.ARMOR_EQUIP_GOLD,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = ModItemTags.REPAIRS_AMETHYST_ARMOR,
        asset = ModEquipmentAssets.AMETHYST,
    )

    @JvmField
    val CRYING: ModArmorMaterial = createArmorMaterial(
        durability = 58,
        boots = 4,
        leggings = 7,
        chestplate = 9,
        helmet = 4,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 1.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.CRYING,
    )

    @JvmField
    val DIRT: ModArmorMaterial = createArmorMaterial(
        durability = 3,
        boots = 1,
        leggings = 1,
        chestplate = 2,
        helmet = 1,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.DIRT,
    )

    @JvmField
    val EMERALD: ModArmorMaterial = createArmorMaterial(
        durability = 14,
        boots = 2,
        leggings = 4,
        chestplate = 5,
        helmet = 2,
        enchantability = 30,
        equipSound = SoundEvents.ARMOR_EQUIP_DIAMOND,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.EMERALD,
    )

    @JvmField
    val EVERLASTING_WINTER: ModArmorMaterial = createArmorMaterial(
        durability = 128,
        boots = 3,
        leggings = 6,
        chestplate = 8,
        helmet = 4,
        enchantability = 10,
        equipSound = SoundEvents.ARMOR_EQUIP_GOLD,
        toughness = 2.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.EVERLASTING_WINTER,
    )

    @JvmField
    val FROZEN: ModArmorMaterial = createArmorMaterial(
        durability = 14,
        boots = 2,
        leggings = 4,
        chestplate = 6,
        helmet = 2,
        enchantability = 20,
        equipSound = SoundEvents.ARMOR_EQUIP_GOLD,
        toughness = 0.1f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.FROZEN,
    )

    @JvmField
    val LAVA: ModArmorMaterial = createArmorMaterial(
        durability = 24,
        boots = 2,
        leggings = 5,
        chestplate = 6,
        helmet = 3,
        enchantability = 7,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 0.5f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.LAVA,
    )

    @JvmField
    val PIGLIN: ModArmorMaterial = createArmorMaterial(
        durability = 26,
        boots = 0,
        leggings = 5,
        chestplate = 7,
        helmet = 0,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 2.0f,
        knockbackResistance = 0.1f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.PIGLIN,
    )

    @JvmField
    val RUPERT: ModArmorMaterial = createArmorMaterial(
        durability = 24,
        boots = 1,
        leggings = 3,
        chestplate = 4,
        helmet = 2,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.RUPERT,
    )

    @JvmField
    val SHULKER: ModArmorMaterial = createArmorMaterial(
        durability = 11,
        boots = 0,
        leggings = 0,
        chestplate = 0,
        helmet = 3,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_CHAIN,
        toughness = 2.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.SHULKER,
    )

    @JvmField
    val STEEL: ModArmorMaterial = createArmorMaterial(
        durability = 26,
        boots = 3,
        leggings = 5,
        chestplate = 7,
        helmet = 3,
        enchantability = 10,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 1.0f,
        knockbackResistance = 0.1f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.STEEL,
    )

    @JvmField
    val STONE: ModArmorMaterial = createArmorMaterial(
        durability = 4,
        boots = 1,
        leggings = 2,
        chestplate = 3,
        helmet = 1,
        enchantability = 15,
        equipSound = SoundEvents.ARMOR_EQUIP_IRON,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.STONE,
    )

    @JvmField
    val TURTLE: ModArmorMaterial = createArmorMaterial(
        durability = 35,
        boots = 3,
        leggings = 6,
        chestplate = 6,
        helmet = 2,
        enchantability = 9,
        equipSound = SoundEvents.ARMOR_EQUIP_TURTLE,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.TURTLE,
    )

    @JvmField
    val WOOD: ModArmorMaterial = createArmorMaterial(
        durability = 12,
        boots = 1,
        leggings = 2,
        chestplate = 3,
        helmet = 1,
        enchantability = 20,
        equipSound = SoundEvents.ARMOR_EQUIP_LEATHER,
        toughness = 0.0f,
        knockbackResistance = 0.0f,
        repairTag = EMPTY_REPAIR_TAG,
        asset = ModEquipmentAssets.WOOD,
    )

    private fun makeDefense(
        boots: Int,
        leggings: Int,
        chestplate: Int,
        helmet: Int,
        body: Int,
    ): Map<ArmorItem.Type, Int> =
        mapOf(
            ArmorItem.Type.BOOTS to boots,
            ArmorItem.Type.LEGGINGS to leggings,
            ArmorItem.Type.CHESTPLATE to chestplate,
            ArmorItem.Type.HELMET to helmet,
            ArmorItem.Type.BODY to body,
        )
}
