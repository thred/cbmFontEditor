package org.cbm.editor.font.ui.action;

import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.model.Tool;

public class DrawToolAction extends AbstractToolAction
{

    private static final long serialVersionUID = 1577034506977416571L;

    public DrawToolAction()
    {
        super(Tool.DRAW, "Draw Mode", Icon.DRAW_TOOL.getIcon());

        putValue(SHORT_DESCRIPTION, "Tool to invert the pixel and following pixels in the image");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("D"));
    }

}
