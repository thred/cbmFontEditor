package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Block;

public class BlockCharacterModifiedEvent extends BlockModifiedEvent
{

	private final int characterX;
	private final int characterY;

	public BlockCharacterModifiedEvent(final Block block, final int characterX, final int characterY)
	{
		super(block);

		this.characterX = characterX;
		this.characterY = characterY;
	}

	public int getCharacterX()
	{
		return characterX;
	}

	public int getCharacterY()
	{
		return characterY;
	}

}
