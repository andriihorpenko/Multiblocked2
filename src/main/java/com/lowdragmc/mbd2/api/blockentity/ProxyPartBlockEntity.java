package com.lowdragmc.mbd2.api.blockentity;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

/**
 * @author KilaBash
 * @implNote It is used to replace the non mbd blocks that do not need to be rendered after forming in the multiblock structure,
 * and to restore the original blocks when the structure invalid.
 */
public class ProxyPartBlockEntity extends BlockEntity {
    @Getter
    @Setter
    private boolean isAsyncSyncing = false;

    public static RegistryObject<BlockEntityType<ProxyPartBlockEntity>> TYPE;
    public static BlockEntityType<?> TYPE() {
        return TYPE.get();
    }

    @Nullable
    @Getter
    private BlockState originalState;
    @Nullable
    @Getter
    private CompoundTag originalData;
    @Nullable
    @Getter
    private BlockPos controllerPos;

    public ProxyPartBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(TYPE(), pPos, pBlockState);
    }

    public void setControllerData(BlockPos controllerPos) {
        if (this.controllerPos != controllerPos) {
            this.controllerPos = controllerPos;
            sync();
        }
    }

    public void setOriginalData(BlockState originalState, CompoundTag originalData, BlockPos controllerPos) {
        if (this.originalState != originalState || this.originalData != originalData || this.controllerPos != controllerPos) {
            this.originalState = originalState;
            this.originalData = originalData;
            this.controllerPos = controllerPos;
            sync();
        }
    }

    /**
     * Place the original block back to the world. and restore the original block entity data.
     */
    public void restoreOriginalBlock() {
        if (originalState != null) {
            level.setBlockAndUpdate(getBlockPos(), originalState);
            if (originalData != null) {
                var blockEntity = level.getBlockEntity(worldPosition);
                if (blockEntity != null) {
                    blockEntity.load(originalData);
                }
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (originalState != null) {
            tag.put("originalState", NbtUtils.writeBlockState(originalState));
        }

        if (originalData != null) {
            tag.put("originalData", originalData);
        }

        if (controllerPos != null) {
            tag.put("controllerPos", NbtUtils.writeBlockPos(controllerPos));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains("originalState")) {
            originalState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), tag.getCompound("originalState"));
        }

        if (tag.contains("originalData")) {
            originalData = tag.getCompound("originalData");
        }

        if (tag.contains("controllerPos")) {
            controllerPos = NbtUtils.readBlockPos(tag.getCompound("controllerPos"));
        }

    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();

        if (originalState != null) {
            tag.put("originalState", NbtUtils.writeBlockState(originalState));
        }

        if (originalData != null) {
            tag.put("originalData", originalData);
        }

        if (controllerPos != null) {
            tag.put("controllerPos", NbtUtils.writeBlockPos(controllerPos));
        }

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void sync() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 11);
        }
    }

}
