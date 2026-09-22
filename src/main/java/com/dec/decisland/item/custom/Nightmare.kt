package com.dec.decisland.item.custom

import com.dec.decisland.entity.projectile.NightmareRay
import com.dec.decisland.item.compat.ModProjectileUtil
import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class Nightmare(properties: Properties) : Item(properties) {
    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack)
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResultHolder.fail(stack)
        if (ManaManager.getCurrentMana(player) <= MANA_COST) {
            return InteractionResultHolder.fail(stack)
        }

        ManaManager.reduceMana(player, MANA_COST)
        serverLevel.playSound(null, player.x, player.y, player.z, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 1.0f, 1.0f)

        ModProjectileUtil.spawnUsingShoot(
            ::NightmareRay,
            serverLevel,
            stack,
            player,
            player.getViewVector(0.0f).x,
            player.getViewVector(0.0f).y,
            player.getViewVector(0.0f).z,
            RAY_INACCURACY,
            RAY_BASE_SPEED,
        )

        stack.hurtAndBreak(1, player, hand.asEquipmentSlot())
        player.swing(hand, true)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    companion object {
        private const val MANA_COST: Float = 10.0f
        private const val RAY_BASE_SPEED: Float = 0.7f
        private const val RAY_INACCURACY: Float = 1.3f
    }
}
