package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.BlockEvent;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.ui.block.BlockState;
import org.cbm.editor.font.util.Palette;

public class BlockColorAction extends AbstractAction
{

    private static final long serialVersionUID = 1809893400928257978L;

    private final Palette color;
    private final ProjectAdapter projectAdapter;
    private final BlockAdapter blockAdapter;

    private BlockState state;

    public BlockColorAction(Palette color)
    {
        super(color.getName(), color.getIcon());

        this.color = color;

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        blockAdapter = Registry.get(BlockAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Sets the color to " + color.getName());
        putValue(MNEMONIC_KEY, color.getMnemonic());

        setEnabled(false);
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
        setEnabled(projectAdapter.getProject() != null && blockAdapter.getBlock() != null);
    }

    public void setState(BlockState state)
    {
        this.state = state;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (state.isBackground())
        {
            Registry.execute(new BackgroundEdit(state.getBlock(), color));
        }
        else
        {
            Registry.execute(new ForegroundEdit(state.getBlock(), state.getPositionsInCharacter(), color));
        }
    }

}
