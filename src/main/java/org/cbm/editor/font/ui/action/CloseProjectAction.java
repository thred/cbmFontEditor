package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.UIUtils;

public class CloseProjectAction extends AbstractAction
{

    private static final long serialVersionUID = 7643768206946134026L;

    private final ProjectAdapter projectAdapter;

    public CloseProjectAction()
    {
        super("Close Project", Icon.CLOSE.getIcon());

        projectAdapter = Registry.get(ProjectAdapter.class);
        projectAdapter.bind(this);

        putValue(SHORT_DESCRIPTION, "Closes the currently open project");

        updateState();
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        updateState();
    }

    private void updateState()
    {
        setEnabled(projectAdapter.getProject() != null);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Project project = projectAdapter.getProject();

        if (project != null && project.isModified())
        {
            if (!UIUtils.confirm(Registry.get(MainFrameController.class).getView(), "Close Project",
                "You have unsaved changes. Do you really want to close the project?\n\nThis action cannot be undone."))
            {
                return;
            }
        }

        Registry.getUndoManager().discardAllEdits();

        projectAdapter.setProject(null);

        Registry.get(StatusBarController.class).setMessage("Project closed.");
    }

}
