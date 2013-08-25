package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Palette;

public class BackgroundEdit extends AbstractEdit
{

	private static final long serialVersionUID = 7708688005476487002L;

	private final Block block;
	private final Palette color;

	private Palette previousColor;

	public BackgroundEdit(final Block block, final Palette color)
	{
		super("Background");

		this.block = block;
		this.color = color;
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
	 */
	@Override
	public void execute()
	{
		previousColor = block.getBackground();
		block.setBackground(color);
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Background set to " + color);
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
	 */
	@Override
	public void rollback()
	{
		block.setBackground(previousColor);
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Background reverted to " + previousColor);
	}

}
