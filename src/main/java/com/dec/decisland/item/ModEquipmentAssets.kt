package com.dec.decisland.item

import com.dec.decisland.DecIsland
import net.minecraft.resources.ResourceLocation

/**
 * 1.21.1 兼容层：1.21.4+ 的 EquipmentAsset 注册表改为直接使用 ResourceLocation
 * （1.21.1 的 ArmorMaterial.Layer 以 ResourceLocation 定位盔甲纹理）。
 */
object ModEquipmentAssets {
    @JvmField
    val FASHION: ResourceLocation = createId("fashion")

    @JvmField
    val AMETHYST: ResourceLocation = createId("amethyst")

    @JvmField
    val CRYING: ResourceLocation = createId("crying")

    @JvmField
    val DIRT: ResourceLocation = createId("dirt")

    @JvmField
    val EMERALD: ResourceLocation = createId("emerald")

    @JvmField
    val EVERLASTING_WINTER: ResourceLocation = createId("everlasting_winter")

    @JvmField
    val FROZEN: ResourceLocation = createId("frozen")

    @JvmField
    val LAVA: ResourceLocation = createId("lava")

    @JvmField
    val PIGLIN: ResourceLocation = createId("piglin")

    @JvmField
    val RUPERT: ResourceLocation = createId("rupert")

    @JvmField
    val SHULKER: ResourceLocation = createId("shulker")

    @JvmField
    val STEEL: ResourceLocation = createId("steel")

    @JvmField
    val STONE: ResourceLocation = createId("stone")

    @JvmField
    val TURTLE: ResourceLocation = createId("turtle")

    @JvmField
    val WOOD: ResourceLocation = createId("wood")

    private fun createId(name: String): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, name)
}
