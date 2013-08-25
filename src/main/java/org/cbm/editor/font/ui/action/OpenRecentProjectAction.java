package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.main.OpenRecentProject;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Prefs;
import org.cbm.editor.font.util.UIUtils;

public class OpenRecentProjectAction extends AbstractAction
{

	private static final long serialVersionUID = 8254551416165646938L;

	private final int index;

	private File file;

	public OpenRecentProjectAction(final int index)
	{
		super();

		this.index = index;

		putValue(SHORT_DESCRIPTION, "Opens a recent project");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl alt " + (index + 1)));
	}

	public void updateState()
	{
		file = Registry.get(OpenRecentProject.class).getRecent(index);

		if (file == null)
		{
			setEnabled(false);

			putValue(NAME, (index + 1) + " - No recent project");
		}
		else
		{
			setEnabled(true);

			putValue(NAME, (index + 1) + " - " + file.getName());
		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent event)
	{
		File fileToOpen = this.file;

		if (fileToOpen == null)
		{
			return;
		}

		if (!fileToOpen.exists())
		{
			UIUtils.error(Registry.get(MainFrameController.class).getView(), "Open Recent Project", "The file \""
					+ fileToOpen + "\" does not exist.");

			return;
		}

		ProjectAdapter projectAdapter = Registry.get(ProjectAdapter.class);
		Project project = projectAdapter.getProject();

		if ((project != null) && (project.isModified()))
		{
			if (!UIUtils.confirm(Registry.get(MainFrameController.class).getView(), "Open Recent Project",
					"You have unsaved changes. Do you really want to open a project?\n\nThis action cannot be undone."))
			{
				return;
			}
		}

		try
		{
			final Project loadedProject = Project.importProject(fileToOpen);

			Prefs.set("load.path", fileToOpen.getAbsolutePath());
			Registry.get(OpenRecentProject.class).recent(fileToOpen);

			Registry.getUndoManager().discardAllEdits();
			loadedProject.setFile(fileToOpen);
			loadedProject.setModified(false);

			projectAdapter.setProject(loadedProject);

			Registry.get(StatusBarController.class).setMessage("Opened project: " + fileToOpen);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);

			UIUtils.error(Registry.get(MainFrameController.class).getView(), "Open Project...",
					"Failed to read file \"" + fileToOpen + "\".");
		}

	}

}
