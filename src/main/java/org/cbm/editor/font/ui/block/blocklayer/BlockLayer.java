package org.cbm.editor.font.ui.block.blocklayer;

import java.awt.Rectangle;

import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.ui.block.Layer;
import org.cbm.editor.font.util.Palette;

public interface BlockLayer extends Layer
{

	public Font getFont();

	public Block getBlock();

	public int getXInPixel();

	public int getYInPixel();

	public int getWidthInPixel();

	public int getHeightInPixel();

	public int getWidthInCharacters();

	public int getHeightInCharacters();

	public int getCharacterWidth();

	public int getCharacterHeight();

	public boolean isCharacterAvailable(int characterX, int characterY);

	public int getOverscanInPixel();

	public Palette getBackground();

	public void invalidateImage();

	public BlockSelection createBlockSelection(Rectangle rectangle);

}
