package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontSwitchedEvent extends FontEvent
{

    private final Font oldFont;

    public FontSwitchedEvent(final Font oldFont, final Font newFont)
    {
        super(newFont);

        this.oldFont = oldFont;
    }

    public Font getOldFont()
    {
        return oldFont;
    }

}
