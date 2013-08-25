package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class IndexedBlockEvent extends BlockEvent
{

	private final int index;

	public IndexedBlockEvent(Block block, int index)
	{
		super(block);

		this.index = index;
	}

	public int getIndex()
	{
		return index;
	}

}
