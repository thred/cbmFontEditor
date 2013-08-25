package org.cbm.editor.font.ui.block;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;

import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.ui.block.image.DefaultBlockImage;
import org.cbm.editor.font.util.XMLBuilder;

public class BlockSelection
{

	public static final DataFlavor OBJECT_DATA_FLAVOR = new DataFlavor(BlockComponentTransferable.class, "Block Layer");
	public static final DataFlavor IMAGE_DATA_FLAVOR = DataFlavor.imageFlavor;
	public static final DataFlavor TEXT_DATA_FLAVOR = DataFlavor.stringFlavor;

	private final Font font;
	private final Block block;
	private final Rectangle rectangle;

	public BlockSelection(Font font, Block block, Rectangle rectangle)
	{
		super();

		this.font = font;
		this.block = block;
		this.rectangle = rectangle;
	}

	public Font getFont()
	{
		return font;
	}

	public Block getBlock()
	{
		return block;
	}

	public Rectangle getRectangle()
	{
		return rectangle;
	}

	public Image toImage()
	{
		return new DefaultBlockImage(font, block, rectangle).toImage();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		XMLBuilder builder = new XMLBuilder();

		builder.begin("cbm-font-block");

		block.exportBlock(builder);
		font.exportFont(builder);

		builder.end();

		return builder.toString();
	}

}
