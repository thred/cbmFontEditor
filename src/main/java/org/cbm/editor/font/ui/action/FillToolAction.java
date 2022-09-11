package org.cbm.editor.font.ui.action;

import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.model.Tool;

public class FillToolAction extends AbstractToolAction
{

    private static final long serialVersionUID = 1577034506977416571L;

    public FillToolAction()
    {
        super(Tool.FILL, "Fill Mode", Icon.FILL_TOOL.getIcon());

        putValue(SHORT_DESCRIPTION, "Tool to invert all pixels in the closed area of the image");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F"));
    }

}
