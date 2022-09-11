package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class IndexedFontEvent extends FontEvent
{
    private final int index;

    public IndexedFontEvent(final Font font, final int index)
    {
        super(font);

        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

}
