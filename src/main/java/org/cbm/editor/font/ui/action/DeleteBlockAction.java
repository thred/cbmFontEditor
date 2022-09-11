package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.BlockEvent;
import org.cbm.editor.font.model.events.ProjectEvent;

public class DeleteBlockAction extends AbstractAction
{

    private static final long serialVersionUID = -754841748100014203L;

    private final ProjectAdapter projectAdapter;
    private final BlockAdapter blockAdapter;

    public DeleteBlockAction()
    {
        super("Delete Block", Icon.REMOVE.getIcon());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        blockAdapter = Registry.get(BlockAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Removes the selected block");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl MINUS"));

        updateState();
    }

    public void handleEvent(ProjectEvent event)
    {
        updateState();
    }

    public void handleEvent(BlockEvent event)
    {
        updateState();
    }

    public void updateState()
    {
        setEnabled(projectAdapter.getProject() != null && blockAdapter.getBlock() != null);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Block block = blockAdapter.getBlock();

        Registry.execute(new DeleteBlockEdit(block));
    }
}
