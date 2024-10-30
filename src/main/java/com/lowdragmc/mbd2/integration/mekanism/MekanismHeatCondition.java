package com.lowdragmc.mbd2.integration.mekanism;

import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.mbd2.api.capability.recipe.IO;
import com.lowdragmc.mbd2.api.capability.recipe.IRecipeHandler;
import com.lowdragmc.mbd2.api.recipe.MBDRecipe;
import com.lowdragmc.mbd2.api.recipe.RecipeCondition;
import com.lowdragmc.mbd2.api.recipe.RecipeLogic;
import com.lowdragmc.mbd2.integration.mekanism.trait.heat.MekHeatCapabilityTrait;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class MekanismHeatCondition extends RecipeCondition {

    public final static MekanismHeatCondition INSTANCE = new MekanismHeatCondition();
    @Configurable(name = "config.recipe.condition.heat.min")
    @NumberRange(range = {0f, Float.MAX_VALUE})
    private double minHeat;
    @Configurable(name = "config.recipe.condition.heat.max")
    @NumberRange(range = {0f, Float.MAX_VALUE})
    private double maxHeat;


    public MekanismHeatCondition(double minHeat, double maxHeat) {
        this.minHeat = minHeat;
        this.maxHeat = maxHeat;
    }

    @Override
    public String getType() {
        return "mekanism_heat";
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("recipe.condition.mekanism_heat.tooltip", minHeat, maxHeat);
    }

    @Override
    public IGuiTexture getIcon() {
        return new ResourceTexture("mbd2:textures/gui/thermometer.png");
    }

    @Override
    public boolean test(@Nonnull MBDRecipe recipe, @Nonnull RecipeLogic recipeLogic) {
        var proxy = recipeLogic.machine.getRecipeCapabilitiesProxy();
        var toCheck = new ArrayList<IRecipeHandler<?>>();
        if (recipe.inputs.containsKey(MekanismHeatRecipeCapability.CAP)) {
            var inputs = proxy.get(IO.IN, MekanismHeatRecipeCapability.CAP);
            toCheck.addAll(inputs);
        }
        if (recipe.outputs.containsKey(MekanismHeatRecipeCapability.CAP)) {
            var outputs = proxy.get(IO.OUT, MekanismHeatRecipeCapability.CAP);
            toCheck.addAll(outputs);
        }
        toCheck.addAll(proxy.get(IO.BOTH, MekanismHeatRecipeCapability.CAP));
        for (IRecipeHandler<?> handler : toCheck) {
            if (handler instanceof MekHeatCapabilityTrait.HeatRecipeHandler heatRecipeHandler) {
                var heat = ((MekHeatCapabilityTrait)heatRecipeHandler.trait).getContainer().getTemperature(0);
                if (heat >= minHeat && heat <= maxHeat) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nonnull
    @Override
    public JsonObject serialize() {
        JsonObject config = super.serialize();
        config.addProperty("minHeat", minHeat);
        config.addProperty("maxHeat", maxHeat);
        return config;
    }

    @Override
    public RecipeCondition deserialize(@Nonnull JsonObject config) {
        super.deserialize(config);
        minHeat = GsonHelper.getAsDouble(config, "minHeat", 0);
        maxHeat = GsonHelper.getAsDouble(config, "maxHeat", 1);
        return this;
    }

    @Override
    public RecipeCondition fromNetwork(FriendlyByteBuf buf) {
        super.fromNetwork(buf);
        minHeat = buf.readDouble();
        maxHeat = buf.readDouble();
        return this;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        super.toNetwork(buf);
        buf.writeDouble(minHeat);
        buf.writeDouble(maxHeat);
    }

    @Override
    public CompoundTag toNBT() {
        var tag = super.toNBT();
        tag.putDouble("minHeat", minHeat);
        tag.putDouble("maxHeat", maxHeat);
        return tag;
    }

    @Override
    public RecipeCondition fromNBT(CompoundTag tag) {
        super.fromNBT(tag);
        minHeat = tag.getDouble("minHeat");
        maxHeat = tag.getDouble("maxHeat");
        return this;
    }

}
