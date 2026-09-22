package com.dec.decisland.item.compat

import com.dec.decisland.item.ToolMaterial
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.SwordItem
import net.minecraft.world.level.ItemLike

/**
 * 1.21.1 兼容层：模拟 1.21.4+ 的 Item.Properties 组件 API。
 *
 * - sword/axe/pickaxe：以 Tier 属性计算攻击属性与耐久。
 * - useCooldown / enchantable / repairable：1.21.1 无对应组件，暂时为 no-op
 *   （冷却可由物品逻辑自行调用 player.getCooldowns().addCooldown 实现）。
 */
object ItemCompat {
    /** 将 1.21.1 inventoryTick 的槽位下标转换为 EquipmentSlot（非装备槽返回 null）。 */
    @JvmStatic
    fun slotFromIndex(index: Int): EquipmentSlot? = when (index) {
        0 -> EquipmentSlot.MAINHAND
        40 -> EquipmentSlot.OFFHAND
        36 -> EquipmentSlot.FEET
        37 -> EquipmentSlot.LEGS
        38 -> EquipmentSlot.CHEST
        39 -> EquipmentSlot.HEAD
        else -> null
    }
}

fun Item.Properties.sword(
    material: ToolMaterial,
    attackDamage: Float,
    attackSpeed: Float,
): Item.Properties = this
    .durability(material.durability)
    .attributes(SwordItem.createAttributes(material, attackDamage, attackSpeed))

fun Item.Properties.axe(
    material: ToolMaterial,
    attackDamage: Float,
    attackSpeed: Float,
): Item.Properties = this
    .durability(material.durability)
    .attributes(SwordItem.createAttributes(material, attackDamage, attackSpeed))

fun Item.Properties.pickaxe(
    material: ToolMaterial,
    attackDamage: Float,
    attackSpeed: Float,
): Item.Properties = this
    .durability(material.durability)
    .attributes(SwordItem.createAttributes(material, attackDamage, attackSpeed))

fun Item.Properties.useCooldown(seconds: Float): Item.Properties = this

fun Item.Properties.enchantable(value: Int): Item.Properties = this

fun Item.Properties.repairable(item: ItemLike): Item.Properties = this

fun Item.Properties.repairable(tag: TagKey<Item>): Item.Properties = this

fun InteractionHand.asEquipmentSlot(): EquipmentSlot =
    if (this == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
