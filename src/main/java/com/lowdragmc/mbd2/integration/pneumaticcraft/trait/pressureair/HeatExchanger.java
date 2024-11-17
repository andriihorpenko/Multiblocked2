package com.lowdragmc.mbd2.integration.pneumaticcraft.trait.pressureair;

import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware;
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable;
import lombok.Getter;
import lombok.Setter;
import me.desht.pneumaticcraft.common.heat.HeatExchangerLogicTicking;
import net.minecraft.nbt.CompoundTag;

public class HeatExchanger extends HeatExchangerLogicTicking implements ITagSerializable<CompoundTag>, IContentChangeAware {
    @Setter
    @Getter
    public Runnable onContentsChanged = () -> {};

    @Override
    public void setTemperature(double temperature) {
        if (temperature != getTemperature()) {
            super.setTemperature(temperature);
            onContentsChanged.run();
        }
    }

    public void setTemperatureWithoutNotify(double temperature) {
        super.setTemperature(temperature);
    }
}
