package com.dec.decisland.item.custom

import com.dec.decisland.item.ModArmorMaterials
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item

class Mask(properties: Properties) : ArmorItem(
    ModArmorMaterials.FASHION.holder,
    ArmorItem.Type.HELMET,
    properties.durability(ArmorItem.Type.HELMET.getDurability(ModArmorMaterials.FASHION.durabilityMultiplier)),
)
