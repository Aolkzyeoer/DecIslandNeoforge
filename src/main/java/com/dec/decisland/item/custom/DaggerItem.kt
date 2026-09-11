package com.dec.decisland.item.custom

import com.dec.decisland.DecIsland
import com.dec.decisland.events.AccessoryCombatEffects
import com.dec.decisland.item.compat.ItemCompat
import com.dec.decisland.network.Networking
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

// Dagger: right click performs the Bedrock forward thrust skill.
open class DaggerItem(
    properties: Properties,
    protected val config: DaggerConfig,
) : Item(properties) {
    private val movementSpeedModifierId: ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "movement_speed/${config.name}")

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        val stack = player.getItemInHand(hand)
        if (player.cooldowns.isOnCooldown(stack.item)) {
            return InteractionResultHolder.fail(stack)
        }

        if (!level.isClientSide) {
            performThrust(level as ServerLevel, player, stack)
            player.awardStat(Stats.ITEM_USED.get(this))
            AccessoryCombatEffects.onSuccessfulWeaponUse(player, stack)
        }

        player.swing(hand, true)
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
    }

    // Applies the Bedrock thrust: an axis-aligned 0.6x2x0.6 box placed 0.8 blocks ahead of the player.
    protected fun performThrust(serverLevel: ServerLevel, player: Player, stack: ItemStack): Int {
        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND)

        if (config.auraRadius > 0.0) {
            config.auraEffect?.let { auraEffect ->
                val center = player.position()
                val searchBox = AABB(center, center).inflate(config.auraRadius)
                serverLevel.getEntitiesOfClass(LivingEntity::class.java, searchBox)
                    .filter { target -> target !== player && target.isAlive }
                    .forEach { target -> target.addEffect(auraEffect.toMobEffectInstance()) }
            }
            spawnParticle(serverLevel, config.casterParticleId, player.position())
        }

        val targets = findThrustTargets(serverLevel, player)
        targets.forEach { target ->
            dealThrustDamage(serverLevel, player, target, config.skillDamage)
            config.targetEffects.forEach { effect -> target.addEffect(effect.toMobEffectInstance()) }
            spawnParticle(serverLevel, config.targetParticleId, target.position())
        }
        return targets.size
    }

    // Finds all living targets inside the forward thrust box, mirroring Bedrock's `^^^0.8` selector.
    protected fun findThrustTargets(serverLevel: ServerLevel, player: Player): List<LivingEntity> {
        val view = player.getViewVector(0.0f)
        val center = Vec3(player.x, player.y, player.z).add(view.scale(THRUST_DISTANCE))
        val box = AABB(
            center.x - THRUST_HALF_WIDTH,
            center.y - THRUST_Y_BELOW,
            center.z - THRUST_HALF_WIDTH,
            center.x + THRUST_HALF_WIDTH,
            center.y + THRUST_Y_ABOVE,
            center.z + THRUST_HALF_WIDTH,
        )
        return serverLevel.getEntitiesOfClass(LivingEntity::class.java, box)
            .filter { target ->
                target !== player &&
                    target.isAlive &&
                    !(target is ArmorStand && target.isMarker)
            }
    }

    // Deals fixed thrust damage while bypassing the target's current hurt cooldown.
    protected fun dealThrustDamage(serverLevel: ServerLevel, attacker: Player, target: LivingEntity, damage: Float): Boolean {
        if (damage <= 0.0f) {
            return false
        }

        val originalInvulnerableTime = target.invulnerableTime
        target.invulnerableTime = 0
        val hurt = target.hurt(serverLevel.damageSources().playerAttack(attacker), damage)
        if (!hurt) {
            target.invulnerableTime = originalInvulnerableTime
        }
        return hurt
    }

    // Spawns a bedrock emitter at the supplied world position.
    protected fun spawnParticle(serverLevel: ServerLevel, particleId: ResourceLocation?, position: Vec3) {
        if (particleId == null) {
            return
        }
        Networking.sendBedrockEmitterToNearby(serverLevel, particleId, position, 64.0, config.particleDurationTicks)
    }

    // Keeps hand-held movement modifiers in sync while the dagger is equipped.
    override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
        super.inventoryTick(stack, level, entity, slotId, isSelected)
        updateMovementSpeedModifier(entity, ItemCompat.slotFromIndex(slotId))
    }

    // Applies or removes the movement-speed modifier depending on whether the dagger is currently held.
    private fun updateMovementSpeedModifier(entity: Entity, slot: EquipmentSlot?) {
        val player = entity as? Player ?: return
        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
            removeMovementSpeedModifier(player)
            return
        }
        if (config.movementSpeedAddition == 0.0) {
            removeMovementSpeedModifier(player)
            return
        }

        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        val existing = attribute.getModifier(movementSpeedModifierId)
        if (existing != null && existing.amount == config.movementSpeedAddition) {
            return
        }
        if (existing != null) {
            attribute.removeModifier(movementSpeedModifierId)
        }

        attribute.addTransientModifier(
            AttributeModifier(
                movementSpeedModifierId,
                config.movementSpeedAddition,
                AttributeModifier.Operation.ADD_VALUE,
            ),
        )
    }

    // Removes the transient movement-speed modifier if it is present.
    private fun removeMovementSpeedModifier(player: Player) {
        val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return
        if (attribute.getModifier(movementSpeedModifierId) != null) {
            attribute.removeModifier(movementSpeedModifierId)
        }
    }

    class DaggerConfig private constructor(builder: Builder) {
        @JvmField
        val name: String = builder.name

        @JvmField
        val skillDamage: Float = builder.skillDamage

        @JvmField
        val movementSpeedAddition: Double = builder.movementSpeedAddition

        @JvmField
        val targetEffects: List<SickleItem.EffectConfig> = builder.targetEffects.toList()

        @JvmField
        val auraRadius: Double = builder.auraRadius

        @JvmField
        val auraEffect: SickleItem.EffectConfig? = builder.auraEffect

        @JvmField
        val casterParticleId: ResourceLocation? = builder.casterParticleId

        @JvmField
        val targetParticleId: ResourceLocation? = builder.targetParticleId

        @JvmField
        val particleDurationTicks: Int = builder.particleDurationTicks

        class Builder(
            @JvmField val name: String,
            @JvmField val skillDamage: Float,
        ) {
            internal var movementSpeedAddition: Double = 0.0
            internal val targetEffects = mutableListOf<SickleItem.EffectConfig>()
            internal var auraRadius: Double = 0.0
            internal var auraEffect: SickleItem.EffectConfig? = null
            internal var casterParticleId: ResourceLocation? = null
            internal var targetParticleId: ResourceLocation? = null
            internal var particleDurationTicks: Int = 6

            fun movementSpeedAddition(movementSpeedAddition: Double): Builder = apply {
                this.movementSpeedAddition = movementSpeedAddition
            }

            fun targetEffect(effect: SickleItem.EffectConfig): Builder = apply {
                targetEffects += effect
            }

            fun aura(radius: Double, effect: SickleItem.EffectConfig?): Builder = apply {
                auraRadius = radius
                auraEffect = effect
            }

            fun casterParticleId(casterParticleId: ResourceLocation?): Builder = apply {
                this.casterParticleId = casterParticleId
            }

            fun targetParticleId(targetParticleId: ResourceLocation?): Builder = apply {
                this.targetParticleId = targetParticleId
            }

            fun particleDurationTicks(particleDurationTicks: Int): Builder = apply {
                this.particleDurationTicks = particleDurationTicks
            }

            fun build(): DaggerConfig = DaggerConfig(this)
        }
    }

    companion object {
        private const val THRUST_DISTANCE: Double = 0.8
        private const val THRUST_HALF_WIDTH: Double = 0.3
        private const val THRUST_Y_BELOW: Double = 0.4
        private const val THRUST_Y_ABOVE: Double = 1.6
    }
}
