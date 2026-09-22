package com.dec.decisland.item.custom

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class GuideBookItem(properties: Properties) : Item(properties) {
    override fun isFoil(stack: ItemStack): Boolean = true

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack)
        }

        MESSAGE_KEYS.forEach { key ->
            player.displayClientMessage(Component.translatable(key), false)
        }

        return InteractionResultHolder.success(stack)
    }

    companion object {
        private val MESSAGE_KEYS: List<String> = listOf(
            "text.decisland.guide_book_0",
            "text.decisland.guide_book_1",
            "text.decisland.guide_book_2",
            "text.decisland.guide_book_3",
        )
    }
}
