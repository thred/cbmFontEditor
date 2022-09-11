package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.ui.main.StatusBarController;

public class BlockAcceptEdit extends AbstractEdit
{

    private static final long serialVersionUID = -2012377693460397308L;

    private final Block block;
    private final int xInCharacter;
    private final int yInCharacter;
    private final BlockSelection blockSelection;

    private Block backup;

    public BlockAcceptEdit(Block block, int xInCharacter, int yInCharacter, BlockSelection blockSelection)
    {
        super("Accept");

        this.block = block;
        this.xInCharacter = xInCharacter;
        this.yInCharacter = yInCharacter;
        this.blockSelection = blockSelection;
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        backup = block.copy();

        block.paste(xInCharacter, yInCharacter, blockSelection.getBlock());

        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Characters of block updated");
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        block.restore(backup);

        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Characters of block reverted");
    }

}
