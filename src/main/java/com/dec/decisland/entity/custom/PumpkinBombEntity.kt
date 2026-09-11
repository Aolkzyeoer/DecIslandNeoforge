package com.dec.decisland.entity.custom

import com.dec.decisland.entity.ModEntities
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level

class PumpkinBombEntity(entityType: EntityType<PumpkinBombEntity>, level: Level) : Entity(entityType, level) {
    private var fuse = DEFAULT_FUSE

    constructor(level: Level, x: Double, y: Double, z: Double) : this(ModEntities.PUMPKIN_BOMB.get(), level) {
        setPos(x, y, z)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        fuse = if (compound.contains(FUSE_KEY)) compound.getInt(FUSE_KEY) else DEFAULT_FUSE
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        compound.putInt(FUSE_KEY, fuse)
    }

    override fun isPushable(): Boolean = true

    override fun getDefaultGravity(): Double = 0.04

    override fun tick() {
        super.tick()
        if (!level().isClientSide) {
            if (fuse-- <= 0) {
                level().explode(this, x, y, z, EXPLOSION_POWER, true, Level.ExplosionInteraction.MOB)
                discard()
            }
        }
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (amount >= 1.0f) {
            discard()
            return true
        }
        return false
    }

    companion object {
        private const val FUSE_KEY = "Fuse"
        private const val DEFAULT_FUSE = 60
        private const val EXPLOSION_POWER = 2.0f
    }
}