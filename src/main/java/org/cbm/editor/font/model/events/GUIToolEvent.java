package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Tool;

public class GUIToolEvent extends GUIEvent
{

	private final Tool tool;

	public GUIToolEvent(final Tool tool)
	{
		super();

		this.tool = tool;
	}

	public Tool getTool()
	{
		return tool;
	}

}
