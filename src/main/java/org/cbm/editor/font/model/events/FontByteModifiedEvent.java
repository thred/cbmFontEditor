package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Font;

public class FontByteModifiedEvent extends FontModifiedEvent
{

    private final int character;
    private final int y;

    public FontByteModifiedEvent(final Font font, final int character, final int y)
    {
        super(font);

        this.character = character;
        this.y = y;
    }

    public int getCharacter()
    {
        return character;
    }

    public int getY()
    {
        return y;
    }

}
