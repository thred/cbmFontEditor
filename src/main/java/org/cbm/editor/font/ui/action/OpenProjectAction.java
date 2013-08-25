package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.main.OpenRecentProject;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Prefs;
import org.cbm.editor.font.util.UIUtils;

public class OpenProjectAction extends AbstractAction
{

	private static final long serialVersionUID = -6219285733734257032L;

	public OpenProjectAction()
	{
		super("Open Project...", Icon.OPEN.getIcon());

		putValue(SHORT_DESCRIPTION, "Loads a project from disk");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent event)
	{
		ProjectAdapter projectAdapter = Registry.get(ProjectAdapter.class);
		Project project = projectAdapter.getProject();

		if ((project != null) && (project.isModified()))
		{
			if (!UIUtils.confirm(Registry.get(MainFrameController.class).getView(), "Open Project...",
					"You have unsaved changes. Do you really want to open a project?\n\nThis action cannot be undone."))
			{
				return;
			}
		}

		final File path = new File(Prefs.get("load.path", System.getProperty("user.home")));

		final JFileChooser chooser = new JFileChooser();
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");

		chooser.setName("Open Project...");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(path);

		final int result = chooser.showOpenDialog(Registry.get(MainFrameController.class).getView());

		if (result != JFileChooser.APPROVE_OPTION)
		{
			return;
		}

		final File file = chooser.getSelectedFile();

		if (!file.exists())
		{
			UIUtils.error(Registry.get(MainFrameController.class).getView(), "Open Project...", "The file \"" + file
					+ "\" does not exist.");

			return;
		}

		try
		{
			final Project loadedProject = Project.importProject(file);

			Prefs.set("load.path", file.getAbsolutePath());
			Registry.get(OpenRecentProject.class).recent(file);

			Registry.getUndoManager().discardAllEdits();

			loadedProject.setFile(file);
			loadedProject.setModified(false);

			projectAdapter.setProject(loadedProject);

			Registry.get(StatusBarController.class).setMessage("Opened project: " + file);
		}
		catch (final Exception e)
		{
			e.printStackTrace(System.err);

			UIUtils.error(Registry.get(MainFrameController.class).getView(), "Open Project...",
					"Failed to read file \"" + file + "\".");
		}
	}

}
