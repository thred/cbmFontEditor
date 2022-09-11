package org.cbm.editor.font.ui.action;

import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.model.Tool;

public class PointerToolAction extends AbstractToolAction
{

    private static final long serialVersionUID = 1577034506977416571L;

    public PointerToolAction()
    {
        super(Tool.POINTER, "Pointer Mode", Icon.POINTER_TOOL.getIcon());

        putValue(SHORT_DESCRIPTION, "Tool to drag and drop a character or a selection");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("A"));
    }

}
