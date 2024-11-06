package com.lowdragmc.mbd2.common.trait;

import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSelector;
import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.mbd2.api.capability.recipe.IO;
import lombok.Getter;
import lombok.Setter;

/**
 * A trait definition that have recipe handling capability.
 */
@Getter @Setter
public abstract class RecipeCapabilityTraitDefinition extends TraitDefinition {

    @Configurable(name = "config.definition.trait.recipe_handler", tips = "config.definition.trait.recipe_handler.tooltip")
    @ConfigSelector(candidate = {"IN", "OUT", "NONE"})
    private IO recipeHandlerIO = IO.IN;

    @Configurable(name = "config.definition.trait.distinct", tips = {"config.definition.trait.distinct.tooltip.0", "config.definition.trait.distinct.tooltip.1"})
    private boolean isDistinct;

    @Configurable(name = "config.definition.trait.slot_names", tips = "config.definition.trait.slot_names.tooltip")
    private String[] slotNames = new String[0];
}
