package org.cbm.editor.font.model;

import org.cbm.editor.font.ui.action.AbstractToolAction;
import org.cbm.editor.font.ui.action.DrawToolAction;
import org.cbm.editor.font.ui.action.FillToolAction;
import org.cbm.editor.font.ui.action.PointerToolAction;
import org.cbm.editor.font.ui.action.SelectionToolAction;

public enum Tool
{

    POINTER(PointerToolAction.class),

    SELECTION(SelectionToolAction.class),

    DRAW(DrawToolAction.class),

    FILL(FillToolAction.class);

    private final Class<? extends AbstractToolAction> actionType;

    private Tool(final Class<? extends AbstractToolAction> actionType)
    {
        this.actionType = actionType;
    }

    public Class<? extends AbstractToolAction> getActionType()
    {
        return actionType;
    }

}
