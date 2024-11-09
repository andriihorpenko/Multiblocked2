package com.lowdragmc.mbd2.common.recipe;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ItemStackTexture;
import com.lowdragmc.mbd2.api.recipe.MBDRecipe;
import com.lowdragmc.mbd2.api.recipe.RecipeCondition;
import com.lowdragmc.mbd2.api.recipe.RecipeLogic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Items;

import javax.annotation.Nonnull;

@Getter
@Setter
@NoArgsConstructor
public class RedstoneSignalCondition extends RecipeCondition {

    public final static RedstoneSignalCondition INSTANCE = new RedstoneSignalCondition();
    @Configurable(name = "config.recipe.condition.redstone_signal.signal.min")
    @NumberRange(range = {0f, 15f})
    private int minSignal;
    @Configurable(name = "config.recipe.condition.redstone_signal.signal.max")
    @NumberRange(range = {0f, 15f})
    private int maxSignal;

    public RedstoneSignalCondition(int minSignal, int maxSignal) {
        this.minSignal = minSignal;
        this.maxSignal = maxSignal;
    }

    @Override
    public String getType() {
        return "redstone_signal";
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.redstone_signal.tooltip", minSignal, maxSignal);
    }

    @Override
    public IGuiTexture getIcon() {
        return new ItemStackTexture(Items.REDSTONE_TORCH);
    }

    @Override
    public boolean test(@Nonnull MBDRecipe recipe, @Nonnull RecipeLogic recipeLogic) {
        var pos = recipeLogic.getMachine().getPos();
        var signal = recipeLogic.getMachine().getLevel().getBestNeighborSignal(pos);
        return signal >= minSignal && signal <= maxSignal;
    }

    @Nonnull
    @Override
    public JsonObject serialize() {
        JsonObject config = super.serialize();
        config.addProperty("minSignal", minSignal);
        config.addProperty("maxSignal", maxSignal);
        return config;
    }

    @Override
    public RecipeCondition deserialize(@Nonnull JsonObject config) {
        super.deserialize(config);
        minSignal = GsonHelper.getAsInt(config, "minSignal", 0);
        maxSignal = GsonHelper.getAsInt(config, "maxSignal", 1);
        return this;
    }

    @Override
    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        minSignal = buf.readVarInt();
        maxSignal = buf.readVarInt();
        return this;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeVarInt(minSignal);
        buf.writeVarInt(maxSignal);
    }

    @Override
    public CompoundTag toNBT() {
        var tag = super.toNBT();
        tag.putInt("minSignal", minSignal);
        tag.putInt("maxSignal", maxSignal);
        return tag;
    }

    @Override
    public RecipeCondition fromNBT(CompoundTag tag) {
        super.fromNBT(tag);
        minSignal = tag.getInt("minSignal");
        maxSignal = tag.getInt("maxSignal");
        return this;
    }

}
