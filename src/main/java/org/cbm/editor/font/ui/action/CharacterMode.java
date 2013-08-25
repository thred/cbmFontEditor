package org.cbm.editor.font.ui.action;

public enum CharacterMode
{

	CHARACTER_1_1("Single (1 x 1) characters"),

	CHARACTER_2_1("Double width (2 x 1) characters"),

	CHARACTER_1_2("Double height (1 x 2) characters"),

	CHARACTER_2_2("Double size (2 x 2) characters");

	private final String text;

	private CharacterMode(final String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return text;
	}

}
