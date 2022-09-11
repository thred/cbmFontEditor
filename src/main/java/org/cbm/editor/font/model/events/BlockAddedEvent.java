package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockAddedEvent extends IndexedBlockEvent
{

    public BlockAddedEvent(Block block, int index)
    {
        super(block, index);
    }

}
