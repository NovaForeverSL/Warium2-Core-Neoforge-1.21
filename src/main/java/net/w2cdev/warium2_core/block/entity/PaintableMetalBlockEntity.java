package net.w2cdev.warium2_core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PaintableMetalBlockEntity extends BlockEntity {
    public static final int DEFAULT_COLOR = 0xFFFFFF;
    private static final String COLOR_TAG = "Color";

    private int color = DEFAULT_COLOR;

    public PaintableMetalBlockEntity(final BlockPos pos, final BlockState blockState) {
        super(ModBlockEntities.PAINTABLE_METAL_BLOCK.get(), pos, blockState);
    }

    public int getColor() {
        return this.color;
    }

    public boolean setColor(final int color) {
        final int normalized = color & 0xFFFFFF;
        if (this.color == normalized) {
            return false;
        }
        this.color = normalized;
        return true;
    }

    public boolean clearColor() {
        return this.setColor(DEFAULT_COLOR);
    }

    public void syncColorUpdate() {
        this.setChanged();
        if (this.level == null) {
            return;
        }
        final BlockState state = this.getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_ALL);
        this.level.setBlocksDirty(this.worldPosition, state, state);
    }

    @Override
    protected void saveAdditional(final CompoundTag tag, final HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.color != DEFAULT_COLOR) {
            tag.putInt(COLOR_TAG, this.color);
        }
    }

    @Override
    protected void loadAdditional(final CompoundTag tag, final HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.color = tag.contains(COLOR_TAG) ? tag.getInt(COLOR_TAG) : DEFAULT_COLOR;
    }

    @Override
    protected void applyImplicitComponents(final BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        final DyedItemColor dyedColor = componentInput.get(DataComponents.DYED_COLOR);
        this.color = dyedColor != null ? (dyedColor.rgb() & 0xFFFFFF) : DEFAULT_COLOR;
    }

    @Override
    protected void collectImplicitComponents(final DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        if (this.color != DEFAULT_COLOR) {
            components.set(DataComponents.DYED_COLOR, new DyedItemColor(this.color, true));
        }
    }

    @Override
    public void removeComponentsFromTag(final CompoundTag tag) {
        super.removeComponentsFromTag(tag);
        tag.remove(COLOR_TAG);
    }

    @Override
    public CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(final Connection net, final ClientboundBlockEntityDataPacket pkt, final HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
        this.syncColorUpdate();
    }

    @Override
    public void handleUpdateTag(final CompoundTag tag, final HolderLookup.Provider lookupProvider) {
        super.handleUpdateTag(tag, lookupProvider);
        this.syncColorUpdate();
    }
}
