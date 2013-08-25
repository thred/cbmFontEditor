package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.FontEvent;
import org.cbm.editor.font.model.events.ProjectEvent;

public class ExportAction extends AbstractAction
{

	private static final long serialVersionUID = 7643768206946134026L;

	private final ProjectAdapter projectAdapter;
	private final FontAdapter fontAdapter;

	public ExportAction()
	{
		super("Export...", Icon.EXPORT.getIcon());

		projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
		fontAdapter = Registry.get(FontAdapter.class).bind(this);

		putValue(SHORT_DESCRIPTION, "Export the font to a file");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift E"));

		updateState();
	}

	public void handleEvent(ProjectEvent event)
	{
		updateState();
	}

	public void handleEvent(FontEvent event)
	{
		updateState();
	}

	private void updateState()
	{
		setEnabled((projectAdapter.getProject() != null) && (fontAdapter.getFont() != null));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
	}

}
