package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockRemovedEvent extends IndexedBlockEvent
{

    public BlockRemovedEvent(Block block, int index)
    {
        super(block, index);
    }

}
