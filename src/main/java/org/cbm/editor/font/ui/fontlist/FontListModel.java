package org.cbm.editor.font.ui.fontlist;

import javax.swing.AbstractListModel;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.FontAddedEvent;
import org.cbm.editor.font.model.events.FontMovedEvent;
import org.cbm.editor.font.model.events.FontPropertyModifiedEvent;
import org.cbm.editor.font.model.events.FontRemovedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;

public class FontListModel extends AbstractListModel<Font>
{

	private static final long serialVersionUID = -7114669078148545336L;

	private final ProjectAdapter projectAdapter;

	public FontListModel()
	{
		super();

		projectAdapter = Registry.get(ProjectAdapter.class).bind(this);

		Registry.get(FontAdapter.class).bind(this);
	}

	public void handleEvent(ProjectSwitchedEvent event)
	{
		Project project = projectAdapter.getProject();

		fireContentsChanged(this, 0, (project != null) ? project.getNumberOfFonts() : 0);
	}

	public void handleEvent(FontAddedEvent event)
	{
		fireIntervalAdded(this, event.getIndex(), event.getIndex());
	}

	public void handleEvent(FontMovedEvent event)
	{
		fireIntervalRemoved(this, event.getPreviousIndex(), event.getPreviousIndex());
		fireIntervalAdded(this, event.getIndex(), event.getIndex());
	}

	public void handleEvent(FontRemovedEvent event)
	{
		fireIntervalRemoved(this, event.getIndex(), event.getIndex());
	}

	public void handleEvent(FontPropertyModifiedEvent event)
	{
		int index = projectAdapter.getProject().indexOfFont(event.getFont());

		fireContentsChanged(this, index, index);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize()
	{
		Project project = projectAdapter.getProject();

		return (project != null) ? project.getNumberOfFonts() : 0;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Font getElementAt(final int index)
	{
		Project project = projectAdapter.getProject();

		return (project != null) ? project.getFont(index) : null;
	}

}
