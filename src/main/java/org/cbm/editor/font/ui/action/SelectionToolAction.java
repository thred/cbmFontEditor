package org.cbm.editor.font.ui.action;

import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.model.Tool;

public class SelectionToolAction extends AbstractToolAction
{

	private static final long serialVersionUID = 1577034506977416571L;

	public SelectionToolAction()
	{
		super(Tool.SELECTION, "Selection Mode", Icon.SELECTION_TOOL.getIcon());

		putValue(SHORT_DESCRIPTION, "Tool to select a rectangular area of the image");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("S"));
	}

}
