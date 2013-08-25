package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;

public class MoveFontEdit extends AbstractEdit
{

	private static final long serialVersionUID = 6343826185530308574L;

	private final Font font;
	private final int index;

	private int previousIndex;

	public MoveFontEdit(Font font, int index)
	{
		super("Move font");

		this.font = font;
		this.index = index;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
	 */
	@Override
	public void execute()
	{
		Project project = Registry.get(ProjectAdapter.class).getProject();
		FontAdapter fontAdapter = Registry.get(FontAdapter.class);

		previousIndex = project.moveFont(index, font);
		fontAdapter.setFont(null);
		fontAdapter.setFont(font);
		project.setModified(true);
		Registry.get(StatusBarController.class).setMessage("Moved font to index: " + index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
	 */
	@Override
	public void rollback()
	{
		Project project = Registry.get(ProjectAdapter.class).getProject();
		FontAdapter fontAdapter = Registry.get(FontAdapter.class);

		project.moveFont(previousIndex, font);
		fontAdapter.setFont(null);
		fontAdapter.setFont(font);
		project.setModified(true);
		Registry.get(StatusBarController.class).setMessage("Restored index of font:" + previousIndex);
	}

}
