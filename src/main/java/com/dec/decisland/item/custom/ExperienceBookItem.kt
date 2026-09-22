package com.dec.decisland.item.custom

import com.dec.decisland.item.ModItems
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ExperienceBookItem(properties: Properties) : Item(properties) {
    override fun isFoil(stack: ItemStack): Boolean = true

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack)
        }

        player.giveExperienceLevels(EXPERIENCE_LEVELS_GRANTED)
        stack.consume(1, player)

        if (player.experienceLevel >= EMPTY_BOOK_REWARD_LEVEL) {
            giveOrDrop(player, ItemStack(ModItems.EXPERIENCE_BOOK_EMPTY.get()))
        }

        return InteractionResultHolder.consume(stack)
    }

    private fun giveOrDrop(player: Player, stack: ItemStack) {
        if (!player.addItem(stack)) {
            player.drop(stack, false)
        }
    }

    companion object {
        private const val EXPERIENCE_LEVELS_GRANTED: Int = 2
        private const val EMPTY_BOOK_REWARD_LEVEL: Int = 12
    }
}
