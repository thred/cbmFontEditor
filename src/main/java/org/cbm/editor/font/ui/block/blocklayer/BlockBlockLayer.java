package org.cbm.editor.font.ui.block.blocklayer;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;

import org.cbm.editor.font.CBMCursor;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.Tool;
import org.cbm.editor.font.model.events.BlockModifiedEvent;
import org.cbm.editor.font.model.events.BlockSwitchedEvent;
import org.cbm.editor.font.model.events.FontModifiedEvent;
import org.cbm.editor.font.model.events.FontSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;
import org.cbm.editor.font.ui.block.image.BlockImage;
import org.cbm.editor.font.ui.block.image.DefaultBlockImage;
import org.cbm.editor.font.ui.video.VideoEmulationBlockImage;

public class BlockBlockLayer extends AbstractBlockLayer
{

    private final FontAdapter fontAdapter;
    private final BlockAdapter blockAdapter;

    private BlockImage image;

    public BlockBlockLayer()
    {
        super();

        Registry.get(ProjectAdapter.class).bind(this);

        fontAdapter = Registry.get(FontAdapter.class).bind(this);
        blockAdapter = Registry.get(BlockAdapter.class).bind(this);

        updateState();
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(FontSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(BlockSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(FontModifiedEvent event)
    {
        repaint();
    }

    public void handleEvent(BlockModifiedEvent event)
    {
        repaint();
    }

    private void updateState()
    {
        invalidateImage();

        revalidate();
    }

    @Override
    public void invalidateImage()
    {
        if (image != null)
        {
            image.destroy();
        }

        image = null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getFont()
     */
    @Override
    public Font getFont()
    {
        return fontAdapter.getFont();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getBlock()
     */
    @Override
    public Block getBlock()
    {
        return blockAdapter.getBlock();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.AbstractBlockLayer#getCursor(java.awt.Point,
     *      org.cbm.editor.font.model.Tool)
     */
    @Override
    public Cursor getCursor(Point mousePosition, Tool tool)
    {
        if (tool == Tool.SELECTION)
        {
            return CBMCursor.SELECTION.getCursor();
        }

        return CBMCursor.POINTER.getCursor();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#draw(java.awt.Graphics2D, double)
     */
    @Override
    public void draw(Graphics2D g)
    {
        double zoom = getComponent().getZoom();
        Font font = getFont();

        if (font == null)
        {
            return;
        }

        Block block = blockAdapter.getBlock();

        if (block == null)
        {
            return;
        }

        BlockImage currentImage = image;

        if (currentImage == null)
        {
            currentImage =
                getComponent().isPal() ? new VideoEmulationBlockImage(font, block) : new DefaultBlockImage(font, block);
            image = currentImage;
        }

        currentImage.draw(g, new Point(getXInPixel(), getYInPixel()), zoom);

        drawGrid(g);
    }

}
