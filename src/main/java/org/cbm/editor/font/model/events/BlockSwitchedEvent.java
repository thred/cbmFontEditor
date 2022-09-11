package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockSwitchedEvent extends BlockEvent
{

    private final Block oldBlock;

    public BlockSwitchedEvent(final Block oldBlock, final Block newBlock)
    {
        super(newBlock);

        this.oldBlock = oldBlock;
    }

    public Block getOldBlock()
    {
        return oldBlock;
    }

}
