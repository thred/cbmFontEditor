package org.cbm.editor.font.ui.edit;

import java.awt.Point;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.action.DrawEdit;

public class Draw
{

	private final Font font;
	private final Block block;
	private final DrawEdit edit;
	private final boolean bit;

	private Point location;

	public Draw(final Font font, final Block block, final Point location)
	{
		super();

		this.font = font;
		this.block = block;

		if (block.isXYInBounds(location.x, location.y))
		{
			bit = !block.getBit(font, location.x, location.y);
			edit = new DrawEdit(font, block, bit);
			edit.add(location.x, location.y);
			block.setBit(font, location.x, location.y, bit);
		}
		else
		{
			bit = true;
			edit = new DrawEdit(font, block, bit);
		}

		this.location = location;

		Registry.get(ProjectAdapter.class).getProject().setModified(true);
	}

	public void next(final Point location)
	{
		if (!this.location.equals(location))
		{
			final int vx = location.x - this.location.x;
			final int vy = location.y - this.location.y;
			final int max = Math.max(Math.abs(vx), Math.abs(vy));

			for (int i = 0; i < max; i += 1)
			{
				final int x = location.x - ((vx * i) / max);
				final int y = location.y - ((vy * i) / max);

				if (block.isXYInBounds(x, y))
				{
					edit.add(x, y);
					block.setBit(font, x, y, bit);
				}
			}

			this.location = location;
		}
	}

	public void finish(final Point location)
	{
		if (block.isXYInBounds(location.x, location.y))
		{
			edit.add(location.x, location.y);
			block.setBit(font, location.x, location.y, bit);
		}

		Registry.alreadyExecuted(edit);

		this.location = location;
	}

}
