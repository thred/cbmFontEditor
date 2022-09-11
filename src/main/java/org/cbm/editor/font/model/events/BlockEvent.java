package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockEvent
{

    private final Block block;

    public BlockEvent(final Block block)
    {
        super();

        this.block = block;
    }

    public Block getBlock()
    {
        return block;
    }

}
