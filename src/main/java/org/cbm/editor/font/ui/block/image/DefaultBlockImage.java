package org.cbm.editor.font.ui.block.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.util.Palette;

public class DefaultBlockImage extends AbstractBlockImage
{

    private static final int[] NOT_SET = {0x80, 0x40, 0x20, 0xff};

    private BufferedImage image;

    public DefaultBlockImage(Font font, Block block)
    {
        super(font, block);
    }

    public DefaultBlockImage(Font font, Block block, Rectangle clip)
    {
        super(font, block, clip);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.image.AbstractBlockImage#render(Font, org.cbm.editor.font.model.Block,
     *      Rectangle, double)
     */
    @Override
    protected Image render(Font font, Block block, Rectangle clip, double zoom)
    {
        int posX = clip != null ? clip.x : 0;
        int posY = clip != null ? clip.y : 0;
        final int width = clip != null ? clip.width : block.getWidth();
        final int height = clip != null ? clip.height : block.getHeight();

        if (width <= 0 || height <= 0)
        {
            return null;
        }

        if (image == null || image.getWidth() != width || image.getHeight() != height)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        final WritableRaster raster = image.getRaster();

        for (int y = 0; y < height; y += 1)
        {
            for (int x = 0; x < width; x += 1)
            {
                Palette color = block.getColor(font, posX + x, posY + y);
                raster.setPixel(x, y, color != null ? color.getARGB() : NOT_SET);
            }
        }

        return image;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.image.AbstractBlockImage#paint(java.awt.Graphics2D, java.awt.Point, Rectangle,
     *      double)
     */
    @Override
    protected void paint(Graphics2D g, Point positionInPixel, Rectangle clip, double zoom)
    {
        if (image == null)
        {
            return;
        }

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        final int x = (int) (positionInPixel.x * zoom);
        final int y = (int) (positionInPixel.y * zoom);
        final int width = (int) (imageWidth * zoom);
        final int height = (int) (imageHeight * zoom);

        if (zoom != (int) zoom)
        {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }

        g.drawImage(image, x, y, x + width, y + height, 0, 0, imageWidth, imageHeight, null);
    }

}
