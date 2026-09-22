package com.dec.decisland.network

import com.dec.decisland.DecIsland
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

class RecoilPayload(
    val pitchUpDegrees: Float,
    val yawDegrees: Float,
) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE

    companion object {
        @JvmField
        val ID: ResourceLocation = ResourceLocation.fromNamespaceAndPath(DecIsland.MOD_ID, "recoil_s2c")

        @JvmField
        val TYPE: CustomPacketPayload.Type<RecoilPayload> = CustomPacketPayload.Type(ID)

        @JvmField
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, RecoilPayload> = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            RecoilPayload::pitchUpDegrees,
            ByteBufCodecs.FLOAT,
            RecoilPayload::yawDegrees,
            ::RecoilPayload,
        )
    }
}
