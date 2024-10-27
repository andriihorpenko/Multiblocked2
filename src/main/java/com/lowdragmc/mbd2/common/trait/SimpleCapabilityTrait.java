package com.lowdragmc.mbd2.common.trait;

import com.lowdragmc.mbd2.api.capability.recipe.IO;
import com.lowdragmc.mbd2.common.machine.MBDMachine;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

public abstract class SimpleCapabilityTrait<T> extends RecipeCapabilityTrait implements ICapabilityProviderTrait<T> {

    public SimpleCapabilityTrait(MBDMachine machine, SimpleCapabilityTraitDefinition<T> definition) {
        super(machine, definition);
    }

    @Override
    public SimpleCapabilityTraitDefinition<T> getDefinition() {
        return (SimpleCapabilityTraitDefinition<T>)super.getDefinition();
    }

    @Override
    public Capability<? super T> getCapability() {
        return getDefinition().getCapability();
    }

    @Override
    public IO getCapabilityIO(@Nullable Direction side) {
        var front = getMachine().getFrontFacing().orElse(Direction.NORTH);
        return getDefinition().getCapabilityIO().getIO(front, side);
    }

}
