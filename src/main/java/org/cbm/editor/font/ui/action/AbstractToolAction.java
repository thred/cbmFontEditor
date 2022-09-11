package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.Tool;
import org.cbm.editor.font.model.events.BlockSwitchedEvent;
import org.cbm.editor.font.model.events.FontSwitchedEvent;
import org.cbm.editor.font.model.events.GUIToolEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;

public abstract class AbstractToolAction extends AbstractAction
{

    private static final long serialVersionUID = 1577034506977416571L;

    private final GUIAdapter guiAdapter;
    private final ProjectAdapter projectAdapter;
    private final FontAdapter fontAdapter;
    private final BlockAdapter blockAdapter;
    private final Tool tool;

    public AbstractToolAction(final Tool tool)
    {
        this(tool, null, null);
    }

    public AbstractToolAction(final Tool tool, final String name)
    {
        this(tool, name, null);
    }

    public AbstractToolAction(final Tool tool, final String name, final javax.swing.Icon icon)
    {
        super(name, icon);

        guiAdapter = Registry.get(GUIAdapter.class).bind(this);
        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        fontAdapter = Registry.get(FontAdapter.class).bind(this);
        blockAdapter = Registry.get(BlockAdapter.class).bind(this);

        this.tool = tool;

        updateState();
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(FontSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(BlockSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(GUIToolEvent event)
    {
        updateState();
    }

    private void updateState()
    {
        setEnabled(
            projectAdapter.getProject() != null && (fontAdapter.getFont() != null || blockAdapter.getBlock() != null));

        setSelected(guiAdapter.getTool() == tool);
    }

    public Tool getTool()
    {
        return tool;
    }

    public boolean isSelected()
    {
        return Boolean.TRUE.equals(getValue(SELECTED_KEY));
    }

    public void setSelected(final boolean selected)
    {
        if (isSelected() != selected)
        {
            putValue(SELECTED_KEY, selected);
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (guiAdapter.getTool() == tool)
        {
            guiAdapter.selectLastTool();
        }
        else
        {
            guiAdapter.selectTool(tool);
        }
    }
}
