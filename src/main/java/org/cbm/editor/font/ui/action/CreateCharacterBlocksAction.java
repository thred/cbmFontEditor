package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.ProjectEvent;

public class CreateCharacterBlocksAction extends AbstractAction
{

    private static final long serialVersionUID = -6616785345341082215L;

    private final ProjectAdapter projectAdapter;

    public CreateCharacterBlocksAction()
    {
        super("Add Character Blocks...", Icon.ADD_MORE.getIcon());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Creates and adds blocks for multiple characters");

        updateStatus();
    }

    public void handleEvent(ProjectEvent event)
    {
        updateStatus();
    }

    private void updateStatus()
    {
        setEnabled(projectAdapter.getProject() != null);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        final CreateCharacterBlocksDialog dialog = new CreateCharacterBlocksDialog();

        dialog.setVisible(true);
    }

}
