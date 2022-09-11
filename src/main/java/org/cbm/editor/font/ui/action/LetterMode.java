package org.cbm.editor.font.ui.action;

public enum LetterMode
{

    NONE("Do not create any letters"),

    UNSHIFTED("Uppercase letters for unshifted mode"),

    SHIFTED("Upper- and lowercase letters for shifted mode");

    private final String text;

    private LetterMode(final String text)
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return text;
    }

}
