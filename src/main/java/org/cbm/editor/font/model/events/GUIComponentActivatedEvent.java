package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.GUIComponentType;

public class GUIComponentActivatedEvent extends GUIEvent
{

	private final GUIComponentType componentType;

	public GUIComponentActivatedEvent(GUIComponentType componentType)
	{
		super();

		this.componentType = componentType;
	}

	public GUIComponentType getComponentType()
	{
		return componentType;
	}

}
