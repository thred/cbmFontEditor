package org.cbm.editor.font.model.events;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.cbm.editor.font.model.Font;

public class FontCharactersModifiedEvent extends FontModifiedEvent
{

    public static final Set<Integer> ALL = new HashSet<>();

    static
    {
        for (int i = 0; i < 256; i += 1)
        {
            ALL.add(Integer.valueOf(i));
        }
    }

    private final Set<Integer> characters;

    public FontCharactersModifiedEvent(final Font font, final Set<Integer> characters)
    {
        super(font);

        this.characters = Collections.unmodifiableSet(characters);
    }

    public Set<Integer> getCharacters()
    {
        return characters;
    }

    public boolean containsCharacter(final int character)
    {
        return characters.contains(Integer.valueOf(character));
    }

}
