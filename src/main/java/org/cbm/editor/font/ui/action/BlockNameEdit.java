package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class BlockNameEdit extends AbstractEdit
{

    private static final long serialVersionUID = 6287762120665483471L;

    private final Block block;
    private final String name;

    private String previousName;

    public BlockNameEdit(Block block, String name)
    {
        super("Name block: " + name);

        this.block = block;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.block.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        previousName = block.getName();
        block.setName(name);
        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Changed name of block to: " + name);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.block.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        block.setName(previousName);
        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Restored name of block: " + previousName);
    }

}
