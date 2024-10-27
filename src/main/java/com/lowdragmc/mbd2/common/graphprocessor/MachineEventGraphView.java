package com.lowdragmc.mbd2.common.graphprocessor;

import com.lowdragmc.lowdraglib.gui.graphprocessor.data.BaseGraph;
import com.lowdragmc.lowdraglib.gui.graphprocessor.widget.GraphViewWidget;

import java.util.List;

public class MachineEventGraphView extends GraphViewWidget {
    public MachineEventGraphView(BaseGraph graph, int x, int y, int width, int height) {
        super(graph, x, y, width, height);
    }

    @Override
    protected void setupNodeGroups(List<String> supportNodeGroups) {
        super.setupNodeGroups(supportNodeGroups);
        supportNodeGroups.add("graph_processor.node.mbd2.machine");
    }
}
