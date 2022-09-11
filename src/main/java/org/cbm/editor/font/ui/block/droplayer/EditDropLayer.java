package org.cbm.editor.font.ui.block.droplayer;

import java.awt.Graphics2D;
import java.awt.Point;

import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.ui.block.image.BlockImage;
import org.cbm.editor.font.ui.block.image.DefaultBlockImage;

public class EditDropLayer extends AbstractDropLayer
{

    private BlockImage image = null;

    public EditDropLayer()
    {
        super(1);
    }

    @Override
    public void setBlockSelection(BlockSelection blockSelection, boolean center)
    {
        image = null;

        super.setBlockSelection(blockSelection, center);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.Layer#draw(java.awt.Graphics2D)
     */
    @Override
    public void draw(Graphics2D g)
    {
        BlockSelection blockSelection = getBlockSelection();

        if (blockSelection == null)
        {
            return;
        }

        double zoom = getComponent().getZoom();

        BlockImage currentImage = image;

        if (currentImage == null)
        {
            currentImage = new DefaultBlockImage(blockSelection.getFont(), blockSelection.getBlock(),
                blockSelection.getRectangle());
            image = currentImage;
        }

        currentImage.draw(g, new Point(getXInPixel(), getYInPixel()), zoom);

        super.draw(g);
    }

}
