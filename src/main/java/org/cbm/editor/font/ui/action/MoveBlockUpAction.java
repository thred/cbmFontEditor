package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.BlockSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;

public class MoveBlockUpAction extends AbstractAction
{

    private static final long serialVersionUID = -754841748100014203L;

    private final ProjectAdapter projectAdapter;
    private final BlockAdapter blockAdapter;

    public MoveBlockUpAction()
    {
        super("Move Block Up", Icon.UP.getIcon());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        blockAdapter = Registry.get(BlockAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Moves the selected block up one step");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl UP"));

        updateState();
    }

    private void updateState()
    {
        Block block = blockAdapter.getBlock();
        Project project = projectAdapter.getProject();

        setEnabled(project != null && block != null && project.indexOfBlock(block) > 0);
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(BlockSwitchedEvent event)
    {
        updateState();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Block block = blockAdapter.getBlock();
        int index = projectAdapter.getProject().indexOfBlock(block);

        Registry.execute(new MoveBlockEdit(block, index - 1));
    }

}
