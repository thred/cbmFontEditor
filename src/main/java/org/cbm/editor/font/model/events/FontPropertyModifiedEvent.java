package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontPropertyModifiedEvent extends FontEvent
{

	private final String propertyName;
	private final Object value;

	public FontPropertyModifiedEvent(Font font, String propertyName, Object value)
	{
		super(font);
		this.propertyName = propertyName;
		this.value = value;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public Object getValue()
	{
		return value;
	}
	
}
