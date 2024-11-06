package com.lowdragmc.mbd2.common.graphprocessor.node;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.graphprocessor.annotation.InputPort;
import com.lowdragmc.lowdraglib.gui.graphprocessor.data.trigger.LinearTriggerNode;
import com.lowdragmc.mbd2.common.machine.MBDMachine;
import net.minecraft.core.Direction;

@LDLRegister(name = "set machine signal", group = "graph_processor.node.mbd2.machine")
public class SetSignalNode extends LinearTriggerNode {
    public enum Mode {
        SIGNAL,
        DIRECT_SIGNAL,
        ANALOG,
    }

    @InputPort
    public MBDMachine machine;
    @InputPort
    public Direction side;
    @InputPort
    public int signal;

    // runtime
    @Configurable
    public Mode mode = Mode.SIGNAL;

    @Override
    protected void process() {
        if (machine != null && (side != null || mode == Mode.ANALOG)) {
            switch (mode) {
                case ANALOG -> machine.setAnalogOutputSignal(signal);
                case SIGNAL -> machine.setOutputSignal(signal, side);
                case DIRECT_SIGNAL -> machine.setOutputDirectSignal(signal, side);
            }
        }
    }
}
