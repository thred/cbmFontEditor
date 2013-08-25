package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class FontNameEdit extends AbstractEdit
{

	private static final long serialVersionUID = -2253529508796181195L;

	private final Font font;
	private final String name;

	private String previousName;

	public FontNameEdit(Font font, String name)
	{
		super("Name font: " + name);

		this.font = font;
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
	 */
	@Override
	public void execute()
	{
		previousName = font.getName();
		font.setName(name);
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Changed name of font to: " + name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
	 */
	@Override
	public void rollback()
	{
		font.setName(previousName);
		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Restored name of font: " + previousName);

	}

}
