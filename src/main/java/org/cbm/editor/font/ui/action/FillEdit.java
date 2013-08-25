package org.cbm.editor.font.ui.action;

import java.awt.Point;
import java.awt.Rectangle;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.BitPlane;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Events;

public class FillEdit extends AbstractEdit
{

	private static final long serialVersionUID = 6795625959417448481L;

	private final Font font;
	private final Block block;
	private final Point position;
	private final Rectangle selection;

	private BitPlane backup;

	public FillEdit(Font font, final Block block, final Point position, final Rectangle selection)
	{
		super("Fill");

		this.font = font;
		this.block = block;
		this.position = position;

		final Rectangle blocksRectangle = new Rectangle(0, 0, block.getWidth(), block.getHeight());

		this.selection = (selection == null) ? blocksRectangle : selection.intersection(blocksRectangle);
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
			backup();

			final BitPlane plane = new BitPlane(selection.width, selection.height);
			final boolean bit = block.getBit(font, position.x, position.y);

			plane.set(position.x - selection.x, position.y - selection.y, true);
			block.setBit(font, position.x, position.y, !bit);

			boolean updated = true;

			while (updated)
			{
				updated = false;

				for (int y = 0; y < selection.height; y += 1)
				{
					for (int x = 0; x < selection.width; x += 1)
					{
						updated = fill(plane, x, y, -1, bit, updated);
						updated = fill(plane, selection.width - 1 - x, selection.height - 1 - y, 1, bit, updated);
					}
				}
			}
		}
		finally
		{
			Events.enable();
		}

		block.fireModified();
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
	}

	private boolean fill(final BitPlane plane, final int x, final int y, final int lookup, final boolean bit,
			final boolean updated)
	{
		final int pixelX = selection.x + x;
		final int pixelY = selection.y + y;

		if (block.getCharacter(pixelX / block.getCharacterWidth(), pixelY / block.getCharacterHeight()) < 0)
		{
			return updated;
		}

		if (block.getBit(font, pixelX, pixelY) != bit)
		{
			return updated;
		}

		if (((plane.isXYInBounds(x + lookup, y)) && (plane.is(x + lookup, y)))
				|| ((plane.isXYInBounds(x, y + lookup)) && (plane.is(x, y + lookup))))
		{
			block.setBit(font, pixelX, pixelY, !bit);
			plane.set(x, y, true);

			return true;
		}

		return updated;
	}

	private void backup()
	{
		backup = new BitPlane(selection.width, selection.height);

		for (int y = 0; y < selection.height; y += 1)
		{
			for (int x = 0; x < selection.width; x += 1)
			{
				final int pixelX = selection.x + x;
				final int pixelY = selection.y + y;

				backup.set(x, y, block.getBit(font, pixelX, pixelY));
			}
		}
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

			for (int y = 0; y < selection.height; y += 1)
			{
				for (int x = 0; x < selection.width; x += 1)
				{
					final int pixelX = selection.x + x;
					final int pixelY = selection.y + y;

					block.setBit(font, pixelX, pixelY, backup.is(x, y));
				}
			}
		}
		finally
		{
			Events.enable();
		}

		block.fireModified();
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Fill reverted.");
	}

}
