package com.dec.decisland.item

import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import java.util.function.Supplier

/**
 * 1.21.1 兼容层：以 1.21.4+ `net.minecraft.world.item.ToolMaterial` 的形态，
 * 实现 1.21.1 的 [Tier] 接口，供 [net.minecraft.world.item.SwordItem.createAttributes] 等使用。
 */
class ToolMaterial private constructor(
    private val incorrectBlocksForDrops: TagKey<Block>,
    val durability: Int,
    private val speed: Float,
    private val attackDamageBonus: Float,
    @JvmField val enchantmentValue: Int,
    private val repairIngredientSupplier: Supplier<Ingredient>,
) : Tier {
    constructor(
        incorrectBlocksForDrops: TagKey<Block>,
        durability: Int,
        speed: Float,
        attackDamageBonus: Float,
        enchantmentValue: Int,
        repairItems: TagKey<Item>,
    ) : this(
        incorrectBlocksForDrops,
        durability,
        speed,
        attackDamageBonus,
        enchantmentValue,
        Supplier { Ingredient.of(repairItems) },
    )

    override fun getUses(): Int = durability

    override fun getSpeed(): Float = speed

    override fun getAttackDamageBonus(): Float = attackDamageBonus

    override fun getIncorrectBlocksForDrops(): TagKey<Block> = incorrectBlocksForDrops

    override fun getEnchantmentValue(): Int = enchantmentValue

    override fun getRepairIngredient(): Ingredient = repairIngredientSupplier.get()

    fun durability(): Int = durability

    companion object {
        private val EMPTY_REPAIR: Supplier<Ingredient> = Supplier { Ingredient.EMPTY }

        @JvmField
        val WOOD: ToolMaterial = of(Tiers.WOOD)

        @JvmField
        val STONE: ToolMaterial = of(Tiers.STONE)

        @JvmField
        val COPPER: ToolMaterial = ToolMaterial(
            BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            190,
            5.0f,
            1.0f,
            12,
            Supplier { Ingredient.of(net.minecraft.world.item.Items.COPPER_INGOT) },
        )

        @JvmField
        val IRON: ToolMaterial = of(Tiers.IRON)

        @JvmField
        val DIAMOND: ToolMaterial = of(Tiers.DIAMOND)

        @JvmField
        val GOLD: ToolMaterial = of(Tiers.GOLD)

        @JvmField
        val NETHERITE: ToolMaterial = of(Tiers.NETHERITE)

        private fun of(tier: Tier): ToolMaterial = ToolMaterial(
            tier.incorrectBlocksForDrops,
            tier.uses,
            tier.speed,
            tier.attackDamageBonus,
            tier.enchantmentValue,
            Supplier { tier.repairIngredient },
        )
    }
}
