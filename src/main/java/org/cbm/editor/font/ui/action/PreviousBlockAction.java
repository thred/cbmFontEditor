package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.BlockEvent;
import org.cbm.editor.font.model.events.ProjectEvent;

public class PreviousBlockAction extends AbstractAction
{

	private static final long serialVersionUID = -4873856931543238437L;

	private final ProjectAdapter projectAdapter;
	private final BlockAdapter blockAdapter;

	public PreviousBlockAction()
	{
		super("Previous Block", Icon.LEFT.getIcon());

		projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
		blockAdapter = Registry.get(BlockAdapter.class).bind(this);

		putValue(SHORT_DESCRIPTION, "Switches to the previous block");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl LEFT"));

		updateState();
	}

	public void handleEvent(ProjectEvent event)
	{
		updateState();
	}

	public void handleEvent(BlockEvent event)
	{
		updateState();
	}

	public void updateState()
	{
		Project project = projectAdapter.getProject();
		Block block = blockAdapter.getBlock();

		setEnabled((project != null) && (block != null) && (project.indexOfBlock(block) > 0));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		Project project = projectAdapter.getProject();
		Block block = blockAdapter.getBlock();
		int index = project.indexOfBlock(block);

		blockAdapter.setBlock(project.getBlock(index - 1));
	}

}
