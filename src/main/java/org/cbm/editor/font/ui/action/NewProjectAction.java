package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.UIUtils;

public class NewProjectAction extends AbstractAction
{

	private static final long serialVersionUID = 7643768206946134026L;

	public NewProjectAction()
	{
		super("New Project", Icon.NEW.getIcon());

		putValue(SHORT_DESCRIPTION, "Creates a new empty project");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		ProjectAdapter projectAdapter = Registry.get(ProjectAdapter.class);
		Project project = projectAdapter.getProject();

		if ((project != null) && (project.isModified()))
		{
			if (!UIUtils
					.confirm(Registry.get(MainFrameController.class).getView(), "New Project",
							"You have unsaved changes. Do you really want to start a new project?\n\nThis action cannot be undone."))
			{
				return;
			}
		}

		Registry.getUndoManager().discardAllEdits();

		projectAdapter.setProject(new Project());

		Registry.get(StatusBarController.class).setMessage("Project created.");
	}

}
