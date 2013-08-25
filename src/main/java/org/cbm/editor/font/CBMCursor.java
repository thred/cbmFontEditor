package org.cbm.editor.font;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum CBMCursor
{

	POINTER("pointerToolCursor.png", 1, 1),

	SELECTION("selectionToolCursor.png", 1, 1),

	DRAW("drawToolCursor.png", 1, 30),

	FILL("fillToolCursor.png", 1, 1),
	
	SELECTION_ACCEPT("selectionAcceptCursor.png", 1, 1);

	private static final String PREFIX = "org/cbm/editor/";

	private final String resource;
	private final int x;
	private final int y;

	private Cursor cursor;

	private CBMCursor(final String resource, final int x, final int y)
	{
		this.resource = PREFIX + resource;
		this.x = x;
		this.y = y;
	}

	public Cursor getCursor()
	{
		if (cursor == null)
		{
			Image image;

			try
			{
				image = ImageIO.read(getClass().getClassLoader().getResource(resource));
			}
			catch (final IOException e)
			{
				throw new RuntimeException("Failed to load image: " + resource, e);
			}

			cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(x, y), name());
		}

		return cursor;
	}

}
