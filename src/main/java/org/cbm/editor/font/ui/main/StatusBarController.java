package org.cbm.editor.font.ui.main;

import javax.swing.JComponent;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.ProjectEvent;

public class StatusBarController
{

	private final ProjectAdapter projectAdapter;
	private final StatusBar view;

	public StatusBarController()
	{
		super();

		projectAdapter = Registry.get(ProjectAdapter.class);
		projectAdapter.bind(this);

		view = new StatusBar();

		updateState();
	}

	public JComponent getView()
	{
		return view;
	}

	public void setMessage(final String message)
	{
		view.setMessage(message);
	}

	public void handleEvent(final ProjectEvent event)
	{
		updateState();
	}

	private void updateState()
	{
		Project project = projectAdapter.getProject();

		if (project == null)
		{
			view.setFile("");
			return;
		}

		String file = (project.getFile() != null) ? project.getFile().toString() : "unnamed";

		if (project.isModified())
		{
			file = file + " (modified)";
		}

		view.setFile("File: " + file);
	}

}
