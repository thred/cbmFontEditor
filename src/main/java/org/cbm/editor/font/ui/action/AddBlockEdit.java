package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class AddBlockEdit extends AbstractEdit
{

    private static final long serialVersionUID = 6650633640271610623L;

    private final int index;

    private Block block = null;

    public AddBlockEdit(final int index)
    {
        super("Block added");

        this.index = index;
    }

    /**
     * @see org.cbm.editor.block.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        block = new Block(0, 0);

        project.addBlock(index, block);
        Registry.get(BlockAdapter.class).setBlock(block);
        project.setModified(true);

        Registry.get(StatusBarController.class).setMessage("Block added.");
        Registry.get(BlockAdapter.class).setBlock(block);
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        project.removeBlock(block);
        project.setModified(true);

        Registry.get(StatusBarController.class).setMessage("Reverted addition of block.");
    }

}
