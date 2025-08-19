package com.vanixmc.events.action.impl.clear_variable_action;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;

public class ClearVariableAction extends AbstractAction {
    private final String variableToClear;

    public ClearVariableAction(String variableToClear) {
        this.variableToClear = variableToClear;
    }

    @Override
    public boolean execute(Context context) {
        Context.PersistentData data = context.getEvent() != null ? context.getEvent().getPersistentData() :
                getEvent().getPersistentData();

        Object removed = data.removeContext(variableToClear);
        return removed != null;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String variableToClear = config.getString("variable");
            if (variableToClear.isEmpty()) {
                throw new IllegalArgumentException("Variable to clear cannot be null or empty.");
            }
            return new ClearVariableAction(variableToClear);
        };
    }
}
