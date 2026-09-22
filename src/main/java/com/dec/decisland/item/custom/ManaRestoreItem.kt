package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.item.compat.asEquipmentSlot
import com.dec.decisland.mana.ManaManager
import com.dec.decisland.network.Networking
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class ManaRestoreItem(properties: Properties) : Item(properties) {
    protected abstract val manaRestoreAmount: Float
    protected abstract val useThreshold: Float
    protected open val particleBursts: Int = 1
    protected open val consumeOnSuccessfulUse: Boolean = false
    protected open val damageOnEveryUse: Boolean = false
    protected open val glint: Boolean = false

    override fun isFoil(stack: ItemStack): Boolean = glint || super.isFoil(stack)

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack)
        }

        val serverLevel = level as? ServerLevel ?: return InteractionResultHolder.pass(stack)
        val currentMana = ManaManager.getCurrentMana(player)
        var restored = false

        if (currentMana <= useThreshold) {
            restored = ManaManager.addManaIgnoringMax(player, manaRestoreAmount) > 0.0f
            if (restored) {
                repeat(particleBursts) {
                    Networking.sendBedrockEmitterToNearby(
                        serverLevel,
                        WHITE_STAR_PARTICLE_ID,
                        player.position().add(0.0, 1.0, 0.0),
                        64.0,
                        2,
                    )
                }
                serverLevel.playSound(
                    null,
                    player.x,
                    player.y,
                    player.z,
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS,
                    0.8f,
                    1.15f,
                )
            }
        }

        if (consumeOnSuccessfulUse && restored) {
            stack.shrink(1)
        }

        if (damageOnEveryUse) {
            stack.hurtAndBreak(1, player, hand.asEquipmentSlot())
        }

        if (restored || damageOnEveryUse) {
            player.swing(hand, true)
            return InteractionResultHolder.success(stack)
        }

        return InteractionResultHolder.fail(stack)
    }

    companion object {
        private val WHITE_STAR_PARTICLE_ID: ResourceLocation =
            ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "white_star_particle")
    }
}
