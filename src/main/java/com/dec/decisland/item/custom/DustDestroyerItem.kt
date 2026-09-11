package com.dec.decisland.item.custom

import com.dec.decisland.entity.custom.WitherCloudEntity
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class DustDestroyerItem(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack)
        }
        val serverLevel = level as? ServerLevel ?: return InteractionResultHolder.pass(stack)
        if (ManaManager.getCurrentMana(player) <= MANA_COST) {
            return InteractionResultHolder.fail(stack)
        }
        ManaManager.reduceMana(player, MANA_COST)
        val view = player.getViewVector(0.0f)
        for (distance in CLOUD_DISTANCES) {
            val pos = player.position().add(view.x * distance, view.y * distance, view.z * distance)
            serverLevel.addFreshEntity(WitherCloudEntity(serverLevel, player, pos.x, pos.y, pos.z))
        }
        player.swing(hand, true)
        return InteractionResultHolder.consume(stack)
    }

    companion object {
        private const val MANA_COST: Float = 1.0f
        private val CLOUD_DISTANCES = listOf(5.0, 3.0)
    }
}