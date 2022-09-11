package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class MoveBlockEdit extends AbstractEdit
{

    private static final long serialVersionUID = 6343826185530308574L;

    private final Block block;
    private final int index;

    private int previousIndex;

    public MoveBlockEdit(Block block, int index)
    {
        super("Move block");

        this.block = block;
        this.index = index;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.block.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        Project project = Registry.get(ProjectAdapter.class).getProject();
        BlockAdapter blockAdapter = Registry.get(BlockAdapter.class);

        previousIndex = project.moveBlock(index, block);
        blockAdapter.setBlock(null);
        blockAdapter.setBlock(block);
        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Moved block to index: " + index);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        Project project = Registry.get(ProjectAdapter.class).getProject();
        BlockAdapter blockAdapter = Registry.get(BlockAdapter.class);

        project.moveBlock(previousIndex, block);
        blockAdapter.setBlock(null);
        blockAdapter.setBlock(block);
        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Restored index of block:" + previousIndex);
    }

}
