package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontEvent
{
	private final Font font;

	public FontEvent(final Font font)
	{
		super();

		this.font = font;
	}

	public Font getFont()
	{
		return font;
	}

}
