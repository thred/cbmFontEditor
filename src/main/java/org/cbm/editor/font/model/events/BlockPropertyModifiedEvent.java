package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockPropertyModifiedEvent extends BlockEvent
{

    private final String propertyName;
    private final Object value;

    public BlockPropertyModifiedEvent(Block block, String propertyName, Object value)
    {
        super(block);
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public Object getValue()
    {
        return value;
    }

}
