package org.cbm.editor.font.ui.block.highlight;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Objects;

import org.cbm.editor.font.model.Tool;
import org.cbm.editor.font.ui.block.AbstractLayer;
import org.cbm.editor.font.ui.block.select.Selection;

public abstract class AbstractHighlightLayer extends AbstractLayer
    implements HighlightLayer, MouseListener, MouseMotionListener, MouseWheelListener
{

    private static final Stroke HIGHLIGHT_STROKE =
        new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{4, 4}, 0);
    private static final Stroke HIGHLIGHT_BACKGROUND_STROKE =
        new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private final int steps;

    private Rectangle highlightInPixel = null;

    public AbstractHighlightLayer(int steps)
    {
        super();

        this.steps = steps;
    }

    public Rectangle getHighlightInPixel()
    {
        return highlightInPixel;
    }

    public void setHighlightInPixel(Rectangle highlightInPixel)
    {
        if (!Objects.equals(this.highlightInPixel, highlightInPixel))
        {
            this.highlightInPixel = highlightInPixel;
            getComponent().repaint();
        }
    }

    public void setHighlightPointInPixel(Point highlightInPixel)
    {
        if (highlightInPixel == null)
        {
            setHighlightInPixel(null);
            return;
        }

        Selection selection = getComponent().getSelectionLayer().getSelection();

        if (selection != null && selection.contains(highlightInPixel))
        {
            setHighlightInPixel(selection.getRectangle());
        }
        else
        {
            setHighlightInPixel(new Rectangle(round(highlightInPixel.x), round(highlightInPixel.y), steps, steps));
        }
    }

    public void setHighlightPointInMouse(Point highlightInMouse)
    {
        if (highlightInMouse == null)
        {
            setHighlightInPixel(null);
            return;
        }

        Point highlightInPixel = null;

        if (getComponent().getRootLayer().getBlock() != null)
        {
            highlightInPixel = getComponent().convertFromComponentToPixel(getComponent().transfer(highlightInMouse));
        }

        setHighlightPointInPixel(highlightInPixel);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.Layer#getCursor(java.awt.Point, org.cbm.editor.font.model.Tool)
     */
    @Override
    public Cursor getCursor(Point mousePosition, Tool tool)
    {
        return null;
    }

    protected int round(int value)
    {
        return value >= 0 ? value / steps * steps : (value - steps) / steps * steps;
    }

    @Override
    public void draw(Graphics2D g)
    {
        if (highlightInPixel != null && !getComponent().getDropLayer().hasBlockSelection())
        {
            Rectangle r = getComponent().convertFromPixelToComponent(highlightInPixel);

            g.setStroke(HIGHLIGHT_BACKGROUND_STROKE);
            g.setColor(Color.BLACK);
            g.draw(r);
            g.setStroke(HIGHLIGHT_STROKE);
            g.setColor(Color.YELLOW);
            g.draw(r);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        setHighlightPointInMouse(e.getPoint());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        setHighlightPointInMouse(e.getPoint());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        setHighlightPointInMouse(e.getPoint());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        setHighlightPointInMouse(e.getPoint());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        setHighlightPointInMouse(null);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        setHighlightPointInMouse(e.getPoint());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        setHighlightPointInMouse(e.getPoint());
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        setHighlightPointInMouse(e.getPoint());
    }

}
