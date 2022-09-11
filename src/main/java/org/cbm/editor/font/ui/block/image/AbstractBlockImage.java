package org.cbm.editor.font.ui.block.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.events.BlockModifiedEvent;
import org.cbm.editor.font.model.events.FontModifiedEvent;
import org.cbm.editor.font.util.Events;

public abstract class AbstractBlockImage implements BlockImage
{

    private final Object semaphore = new Object();
    private final Font font;
    private final Block block;
    private final Rectangle clip;

    private boolean modified = true;

    public AbstractBlockImage(Font font, Block block)
    {
        this(font, block, null);
    }

    public AbstractBlockImage(Font font, Block block, Rectangle clip)
    {
        super();

        this.font = font;
        this.block = block;
        this.clip = clip;

        if (font != null)
        {
            Events.bind(font, this);
        }

        if (block != null)
        {
            Events.bind(block, this);
        }
    }

    @Override
    public void destroy()
    {
        if (font != null)
        {
            Events.unbind(font, this);
        }

        if (block != null)
        {
            Events.unbind(block, this);
        }
    }

    public Font getFont()
    {
        return font;
    }

    public Block getBlock()
    {
        return block;
    }

    public Rectangle getClip()
    {
        return clip;
    }

    public void handleEvent(FontModifiedEvent event)
    {
        synchronized (semaphore)
        {
            modified = true;
        }
    }

    public void handleEvent(BlockModifiedEvent event)
    {
        synchronized (semaphore)
        {
            modified = true;
        }
    }

    @Override
    public void draw(Graphics2D g, Point positionInPixel, double zoom)
    {
        if (block == null)
        {
            return;
        }

        synchronized (semaphore)
        {
            Rectangle currentClip = clip;

            if (currentClip == null)
            {
                currentClip = new Rectangle(0, 0, block.getWidth(), block.getHeight());
            }

            if (modified)
            {
                render(font, block, currentClip, zoom);
                modified = false;
            }

            paint(g, positionInPixel, currentClip, zoom);
        }
    }

    protected abstract Image render(Font font, Block block, Rectangle clip, double zoom);

    protected abstract void paint(Graphics2D g, Point positionInPixel, Rectangle clip, double zoom);

    public Image toImage()
    {
        Rectangle currentClip = clip;

        if (currentClip == null)
        {
            currentClip = new Rectangle(0, 0, block.getWidth(), block.getHeight());
        }

        return render(font, block, currentClip, 1);
    }
}
