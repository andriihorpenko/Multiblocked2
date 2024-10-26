package com.lowdragmc.mbd2.common.machine.definition.config;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.NumberRange;
import com.lowdragmc.lowdraglib.gui.editor.configurator.IConfigurable;
import com.lowdragmc.lowdraglib.syncdata.IPersistedSerializable;
import com.lowdragmc.mbd2.common.trait.TraitDefinition;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Builder
public class ConfigMachineSettings implements IPersistedSerializable, IConfigurable {
    @Getter
    @Builder.Default
    @Configurable(name = "config.machine_settings.machine_level", tips = "config.machine_settings.machine_level.tooltip")
    @NumberRange(range = {0, Integer.MAX_VALUE})
    private int machineLevel = 0;
    @Getter
    @Builder.Default
    @Configurable(name = "config.machine_settings.has_ui", tips = "config.machine_settings.has_ui.tooltip")
    private boolean hasUI = true;
    @Singular
    @NonNull
    @Getter
    private List<TraitDefinition> traitDefinitions;

    @Override
    public CompoundTag serializeNBT() {
        var tag = IPersistedSerializable.super.serializeNBT();
        var traits = new ListTag();
        for (var definition : traitDefinitions) {
            traits.add(TraitDefinition.serializeDefinition(definition));
        }
        tag.put("traitDefinitions", traits);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        IPersistedSerializable.super.deserializeNBT(tag);
        var traits = tag.getList("traitDefinitions", 10);
        traitDefinitions = new ArrayList<>();
        for (var i = 0; i < traits.size(); i++) {
            var trait = traits.getCompound(i);
            var definition = TraitDefinition.deserializeDefinition(trait);
            if (definition != null) {
                traitDefinitions.add(definition);
            }
        }
    }

    public void addTraitDefinition(TraitDefinition definition) {
        traitDefinitions = new ArrayList<>(traitDefinitions);
        traitDefinitions.add(definition);
    }

    public void removeTraitDefinition(TraitDefinition definition) {
        traitDefinitions = this.traitDefinitions.stream().filter(s -> s != definition).toList();
    }

}
