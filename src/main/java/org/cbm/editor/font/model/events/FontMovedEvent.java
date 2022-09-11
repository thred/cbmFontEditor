package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontMovedEvent extends IndexedFontEvent
{

    private final int previousIndex;

    public FontMovedEvent(Font font, int index, int previousIndex)
    {
        super(font, index);

        this.previousIndex = previousIndex;
    }

    public int getPreviousIndex()
    {
        return previousIndex;
    }

}
