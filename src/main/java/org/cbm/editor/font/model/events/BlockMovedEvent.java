package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockMovedEvent extends IndexedBlockEvent
{

	private final int previousIndex;

	public BlockMovedEvent(Block block, int index, int previousIndex)
	{
		super(block, index);

		this.previousIndex = previousIndex;
	}

	public int getPreviousIndex()
	{
		return previousIndex;
	}

}
