package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class DeleteBlockEdit extends AbstractEdit
{

    private static final long serialVersionUID = 6650633640271610623L;

    private final Block block;
    private int index;

    public DeleteBlockEdit(Block block)
    {
        super("Block deleted");

        this.block = block;
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        index = project.removeBlock(block);

        int indexToShow = index > 0 ? index - 1 : 0;
        Registry.get(BlockAdapter.class).setBlock(
            indexToShow < project.getNumberOfBlocks() ? project.getBlock(indexToShow) : null);

        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Block deleted.");
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        final Project project = Registry.get(ProjectAdapter.class).getProject();

        project.addBlock(index, block);
        project.setModified(true);
        Registry.get(StatusBarController.class).setMessage("Reverted deletion of block.");
    }

}
