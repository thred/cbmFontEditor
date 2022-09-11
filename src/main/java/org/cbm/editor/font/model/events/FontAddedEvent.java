package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontAddedEvent extends IndexedFontEvent
{

    public FontAddedEvent(Font font, int index)
    {
        super(font, index);
    }

}
