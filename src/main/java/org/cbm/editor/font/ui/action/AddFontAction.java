package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.ProjectEvent;

public class AddFontAction extends AbstractAction
{

	private static final long serialVersionUID = -754841748100014203L;

	private final ProjectAdapter projectAdapter;

	public AddFontAction()
	{
		super("Add Font", Icon.ADD.getIcon());

		projectAdapter = Registry.get(ProjectAdapter.class);
		projectAdapter.bind(this);

		putValue(SHORT_DESCRIPTION, "Adds a new font to the project");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift PLUS"));

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
		Project project = projectAdapter.getProject();
		int index = project.indexOfFont(Registry.get(FontAdapter.class).getFont());

		if (index < 0)
		{
			index = project.getNumberOfFonts();
		}
		else
		{
			index += 1;
		}

		Registry.execute(new AddFontEdit(index));
	}

}
