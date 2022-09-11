package org.cbm.editor.font.ui.block.blocklayer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.ui.block.AbstractLayer;
import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.util.Palette;

public abstract class AbstractBlockLayer extends AbstractLayer implements BlockLayer
{

    private static final Icon[] ICONS = {
        Icon.GRID0,
        Icon.GRID1,
        Icon.GRID2,
        Icon.GRID3,
        Icon.GRID4,
        Icon.GRID5,
        Icon.GRID6,
        Icon.GRID7,
        Icon.GRID8,
        Icon.GRID9,
        Icon.GRID10,
        Icon.GRID11,
        Icon.GRID12,
        Icon.GRID13,
        Icon.GRID14,
        Icon.GRID15};

    public AbstractBlockLayer()
    {
        super();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getXInPixel()
     */
    @Override
    public int getXInPixel()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getYInPixel()
     */
    @Override
    public int getYInPixel()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getWidthInPixel()
     */
    @Override
    public int getWidthInPixel()
    {
        Block block = getBlock();

        return block != null ? block.getWidth() : 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getHeightInPixel()
     */
    @Override
    public int getHeightInPixel()
    {
        Block block = getBlock();

        return block != null ? block.getHeight() : 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getWidthInCharacters()
     */
    @Override
    public int getWidthInCharacters()
    {
        return getWidthInPixel() / 8;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getHeightInCharacters()
     */
    @Override
    public int getHeightInCharacters()
    {
        return getHeightInPixel() / 8;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getCharacterWidth()
     */
    @Override
    public int getCharacterWidth()
    {
        return getBlock().getCharacterWidth();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getCharacterHeight()
     */
    @Override
    public int getCharacterHeight()
    {
        return getBlock().getCharacterHeight();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#isCharacterAvailable(int, int)
     */
    @Override
    public boolean isCharacterAvailable(int characterX, int characterY)
    {
        Block block = getBlock();

        if (block != null && block.isCharacterXYInBounds(characterX, characterY))
        {
            return block.getCharacter(characterX, characterY) >= 0;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getOverscanInPixel()
     */
    @Override
    public int getOverscanInPixel()
    {
        return 8;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getBackground()
     */
    @Override
    public Palette getBackground()
    {
        Block block = getBlock();

        return block != null ? block.getBackground() : null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#createBlockSelection(java.awt.Rectangle)
     */
    @Override
    public BlockSelection createBlockSelection(Rectangle rectangle)
    {
        Font font = getFont();

        if (font == null)
        {
            return null;
        }

        Block block = getBlock();

        if (block == null)
        {
            return null;
        }

        int x = (int) Math.floor((double) rectangle.x / block.getCharacterWidth());
        int y = (int) Math.floor((double) rectangle.y / block.getCharacterHeight());
        int w = (int) Math.ceil((double) (rectangle.x + rectangle.width) / block.getCharacterWidth()) - x;
        int h = (int) Math.ceil((double) (rectangle.y + rectangle.height) / block.getCharacterHeight()) - y;

        Font copiedFont = font.copy();
        Block copiedBlock = block.copy(x, y, w, h);
        Rectangle copiedRectangle = new Rectangle(rectangle.x - x * block.getCharacterWidth(),
            rectangle.y - y * block.getCharacterHeight(), rectangle.width, rectangle.height);

        return new BlockSelection(copiedFont, copiedBlock, copiedRectangle);
    }

    private Image getGridImage(final int x, final int y)
    {
        final int value = (isCharacterAvailable(x - 1, y - 1) ? 1 : 0)
            + (isCharacterAvailable(x, y - 1) ? 2 : 0)
            + (isCharacterAvailable(x - 1, y) ? 4 : 0)
            + (isCharacterAvailable(x, y) ? 8 : 0);

        return ICONS[value].getImage();
    }

    protected void repaint()
    {
        if (getComponent() != null)
        {
            getComponent().repaint();
        }
    }

    protected void revalidate()
    {
        if (getComponent() != null)
        {
            getComponent().resized();
        }
    }

    protected void drawGrid(Graphics2D g)
    {
        double zoom = getComponent().getZoom();

        if (zoom >= 5)
        {
            // PERFORMANCE - this could be changed to only render those grid points, that are really visible
            for (int y = 0; y <= getHeightInCharacters(); y += 1)
            {
                for (int x = 0; x <= getWidthInCharacters(); x += 1)
                {
                    final Image gridImage = getGridImage(x, y);

                    g.drawImage(gridImage,
                        (int) ((getXInPixel() + x * getCharacterWidth()) * zoom - gridImage.getWidth(null) / 2),
                        (int) ((getYInPixel() + y * getCharacterHeight()) * zoom - gridImage.getHeight(null) / 2),
                        null);
                }
            }
        }
    }

}
