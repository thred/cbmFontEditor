package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockBitModifiedEvent extends BlockModifiedEvent
{

	private final int x;
	private final int y;

	public BlockBitModifiedEvent(final Block block, final int x, final int y)
	{
		super(block);

		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

}
