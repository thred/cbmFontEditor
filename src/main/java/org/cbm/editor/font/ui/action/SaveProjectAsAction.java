package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.ui.main.MainFrameController;
import org.cbm.editor.font.ui.main.OpenRecentProject;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Prefs;
import org.cbm.editor.font.util.UIUtils;

public class SaveProjectAsAction extends AbstractAction
{

	private static final long serialVersionUID = -1527241049347402394L;

	private final ProjectAdapter projectAdapter;

	public SaveProjectAsAction()
	{
		super("Save Project As...", Icon.SAVEAS.getIcon());

		projectAdapter = Registry.get(ProjectAdapter.class);
		projectAdapter.bind(this);

		putValue(SHORT_DESCRIPTION, "Opens the file dialog and saves the project to the selected file");

		updateState();
	}

	public void handleEvent(ProjectEvent event)
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
	public void actionPerformed(final ActionEvent event)
	{
		final Project project = projectAdapter.getProject();

		File file = project.getFile();
		boolean foundFile = false;

		while (!foundFile)
		{
			final File path = new File(Prefs.get("save.path",
					(file != null) ? file.getAbsolutePath() : System.getProperty("user.home")));

			final JFileChooser chooser = new JFileChooser();
			final FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");

			chooser.setName("Save Project As...");
			chooser.setFileFilter(filter);
			chooser.setSelectedFile(path);

			final int result = chooser.showSaveDialog(Registry.get(MainFrameController.class).getView());

			if (result != JFileChooser.APPROVE_OPTION)
			{
				return;
			}

			file = chooser.getSelectedFile();

			if (!file.exists())
			{
				final int index = file.getName().lastIndexOf('.');

				if (index < 0)
				{
					file = new File(file.toString() + ".xml");
				}
			}

			if (file.exists())
			{
				final int confirm = UIUtils.confirmOrAbort(Registry.get(MainFrameController.class).getView(),
						"Save Project As...", "The file \"" + file
								+ "\" already exists.\nDo you want to overwrite the file?");

				if (confirm == JOptionPane.CANCEL_OPTION)
				{
					return;
				}

				foundFile = (confirm == JOptionPane.YES_OPTION);
			}
			else
			{
				foundFile = true;
			}
		}

		Prefs.set("save.path", file.getAbsolutePath());

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

			UIUtils.error(Registry.get(MainFrameController.class).getView(), "Save Project As...",
					"Failed to write file \"" + file + "\".");
		}
	}

}
