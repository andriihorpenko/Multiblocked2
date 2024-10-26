package com.lowdragmc.mbd2.common.network.packets;

import com.lowdragmc.lowdraglib.networking.IHandlerContext;
import com.lowdragmc.lowdraglib.networking.IPacket;
import com.lowdragmc.mbd2.client.renderer.MultiblockInWorldPreviewRenderer;
import com.lowdragmc.mbd2.config.ConfigHolder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@NoArgsConstructor
@AllArgsConstructor
public class SPatternErrorPosPacket implements IPacket {
    public BlockPos pos;

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public void decode(FriendlyByteBuf buf) {
        pos = buf.readBlockPos();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void execute(IHandlerContext handler) {
        MultiblockInWorldPreviewRenderer.showPatternErrorPos(pos, ConfigHolder.multiblockPatternErrorPosDuration * 20);
    }
}
