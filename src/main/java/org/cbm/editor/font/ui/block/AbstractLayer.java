package org.cbm.editor.font.ui.block;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.Tool;

public abstract class AbstractLayer implements Layer
{

    private BlockComponent component;

    public AbstractLayer()
    {
        super();
    }

    public BlockComponent getComponent()
    {
        return component;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.Layer#setComponent(org.cbm.editor.font.ui.block.BlockComponent)
     */
    @Override
    public void setComponent(BlockComponent component)
    {
        this.component = component;
    }

    protected Tool getTool()
    {
        return Registry.get(GUIAdapter.class).getTool();
    }

}
