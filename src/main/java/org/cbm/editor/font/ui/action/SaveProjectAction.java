package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.main.OpenRecentProject;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.UIUtils;

public class SaveProjectAction extends AbstractAction
{

	private static final long serialVersionUID = -1527241049347402394L;

	private final ProjectAdapter projectAdapter;

	public SaveProjectAction()
	{
		super("Save Project", Icon.SAVE.getIcon());

		projectAdapter = Registry.get(ProjectAdapter.class);
		projectAdapter.bind(this);

		putValue(SHORT_DESCRIPTION, "Saves the project");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));

		updateState();
	}

	public void handleEvent(ProjectEvent event)
	{
		updateState();
	}

	private void updateState()
	{
		setEnabled((projectAdapter.getProject() != null) && (projectAdapter.getProject().isModified()));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent event)
	{
		final Project project = projectAdapter.getProject();

		if (project == null)
		{
			return;
		}

		final File file = project.getFile();

		if (file == null)
		{
			Registry.get(SaveProjectAsAction.class).actionPerformed(event);

			return;
		}

		try
		{
			project.exportProject(file);
			Registry.get(OpenRecentProject.class).recent(file);

			project.setFile(file);
			project.setModified(false);
			Registry.get(StatusBarController.class).setMessage("Project saved: " + file);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);

			UIUtils.error(Registry.get(MainFrameController.class).getView(), "Save Project", "Failed to write file \""
					+ file + "\".");
		}
	}

}
