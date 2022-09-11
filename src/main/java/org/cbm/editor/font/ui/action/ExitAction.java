package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.util.UIUtils;

public class ExitAction extends AbstractAction
{

    private static final long serialVersionUID = 7643768206946134026L;

    public ExitAction()
    {
        super("Exit");

        putValue(SHORT_DESCRIPTION, "Quits the CBM Font Editor");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("alt F4"));
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Project project = Registry.get(ProjectAdapter.class).getProject();

        if (project != null && project.isModified())
        {
            if (!UIUtils.confirm(Registry.get(MainFrameController.class).getView(), "Exit",
                "You have unsaved changes. Do you really want to exit the application?"))
            {
                return;
            }
        }

        System.exit(0);
    }

}
