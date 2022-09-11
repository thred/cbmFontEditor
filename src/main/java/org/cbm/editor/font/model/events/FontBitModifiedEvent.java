package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontBitModifiedEvent extends FontByteModifiedEvent
{

    private final int x;

    public FontBitModifiedEvent(Font font, int character, int y, int x)
    {
        super(font, character, y);

        this.x = x;
    }

    public int getX()
    {
        return x;
    }

}
