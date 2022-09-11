package org.cbm.editor.font.ui.blocklist;

import javax.swing.AbstractListModel;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.BlockAddedEvent;
import org.cbm.editor.font.model.events.BlockMovedEvent;
import org.cbm.editor.font.model.events.BlockPropertyModifiedEvent;
import org.cbm.editor.font.model.events.BlockRemovedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;

public class BlockListModel extends AbstractListModel<Block>
{

    private static final long serialVersionUID = -7114669078148545336L;

    private final ProjectAdapter projectAdapter;

    public BlockListModel()
    {
        super();

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);

        Registry.get(BlockAdapter.class).bind(this);
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        Project project = projectAdapter.getProject();

        fireContentsChanged(this, 0, project != null ? project.getNumberOfBlocks() : 0);
    }

    public void handleEvent(BlockAddedEvent event)
    {
        fireIntervalAdded(this, event.getIndex(), event.getIndex());
    }

    public void handleEvent(BlockMovedEvent event)
    {
        fireIntervalRemoved(this, event.getPreviousIndex(), event.getPreviousIndex());
        fireIntervalAdded(this, event.getIndex(), event.getIndex());
    }

    public void handleEvent(BlockRemovedEvent event)
    {
        fireIntervalRemoved(this, event.getIndex(), event.getIndex());
    }

    public void handleEvent(BlockPropertyModifiedEvent event)
    {
        int index = projectAdapter.getProject().indexOfBlock(event.getBlock());

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

        return project != null ? project.getNumberOfBlocks() : 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see javax.swing.ListModel#getElementAt(int)
     */
    @Override
    public Block getElementAt(final int index)
    {
        Project project = projectAdapter.getProject();

        return project != null ? project.getBlock(index) : null;
    }

}
