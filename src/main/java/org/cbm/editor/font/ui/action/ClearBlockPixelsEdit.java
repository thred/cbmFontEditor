package org.cbm.editor.font.ui.action;

import java.awt.Rectangle;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Events;

public class ClearBlockPixelsEdit extends AbstractEdit
{

	private static final long serialVersionUID = 5000443728662903992L;

	private final Font font;
	private final Block block;
	private final Rectangle rectangle;

	private boolean[] backup;

	public ClearBlockPixelsEdit(Font font, Block block, Rectangle rectangle)
	{
		super("Clear Block's Pixels");

		this.font = font;
		this.block = block;
		this.rectangle = rectangle;
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
	 */
	@Override
	public void execute()
	{
		Events.disable();

		try
		{
			backup = new boolean[rectangle.width * rectangle.height];

			for (int y = 0; y < rectangle.height; y += 1)
			{
				for (int x = 0; x < rectangle.width; x += 1)
				{
					final int pixelX = rectangle.x + x;
					final int pixelY = rectangle.y + y;

					if (block.isXYInBounds(pixelX, pixelY))
					{
						backup[x + (y * rectangle.width)] = block.getBit(font, pixelX, pixelY);
						block.setBit(font, pixelX, pixelY, false);
					}
				}
			}
		}
		finally
		{
			Events.enable();
		}

		font.fireModified();
		block.fireModified();
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
	 */
	@Override
	public void rollback()
	{
		Events.disable();

		try
		{
			for (int y = 0; y < rectangle.height; y += 1)
			{
				for (int x = 0; x < rectangle.width; x += 1)
				{
					final int pixelX = rectangle.x + x;
					final int pixelY = rectangle.y + y;

					if (block.isXYInBounds(pixelX, pixelY))
					{
						block.setBit(font, pixelX, pixelY, backup[x + (y * rectangle.width)]);
					}
				}
			}
		}
		finally
		{
			Events.enable();
		}

		font.fireModified();
		block.fireModified();
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Reverted clear block's pixels");
	}

}
