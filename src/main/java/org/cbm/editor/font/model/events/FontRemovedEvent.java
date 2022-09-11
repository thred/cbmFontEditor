package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontRemovedEvent extends IndexedFontEvent
{

    public FontRemovedEvent(Font font, int index)
    {
        super(font, index);
    }

}
