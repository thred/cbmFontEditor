package org.cbm.editor.font.ui.block.select;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.Tool;
import org.cbm.editor.font.model.events.GUISelectionEvent;
import org.cbm.editor.font.ui.block.AbstractLayer;
import org.cbm.editor.font.ui.block.Layer;
import org.cbm.editor.font.util.Events;

public class SelectionLayer extends AbstractLayer implements Layer, MouseListener, MouseMotionListener
{

    private final int steps;

    private Selection selection = null;
    private Selector selector = null;

    public SelectionLayer(int steps)
    {
        super();

        this.steps = steps;
    }

    protected Selection createSelection(final Point positionInComponent)
    {
        return new Selection(this, positionInComponent, steps);
    }

    public Selection getSelection()
    {
        return selection;
    }

    public void setSelection(final Selection selection)
    {
        if (this.selection != selection)
        {
            this.selection = selection;
            getComponent().repaint();
            Events.fire(Registry.get(GUIAdapter.class), new GUISelectionEvent());
        }
    }

    public void clearSelection()
    {
        setSelection(null);
        getComponent().updateCursor();
    }

    public boolean isInSelection(int xInPixel, int yInPixel)
    {
        if (selection == null)
        {
            return false;
        }

        return selection.contains(xInPixel, yInPixel);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.Layer#getCursor(java.awt.Point, org.cbm.editor.font.model.Tool)
     */
    @Override
    public Cursor getCursor(Point mousePosition, Tool tool)
    {
        if (selector != null)
        {
            return Cursor.getPredefinedCursor(selector.getCursor());
        }

        if (selection != null)
        {
            return selection.getCursor(mousePosition);
        }

        return null;
    }

    @Override
    public void draw(Graphics2D g)
    {
        if (selection != null)
        {
            selection.draw(g);
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent event)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent event)
    {
        if (event.isConsumed())
        {
            return;
        }

        if (!getComponent().isSelectionAllowed())
        {
            return;
        }

        final Point positionInComponent = getComponent().transfer(event.getPoint());
        final int modifiers = event.getModifiers();

        if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
        {
            final Selection selection = getSelection();
            final Selector selector = selection != null ? selection.getSelector(positionInComponent) : null;

            if (selector != null)
            {
                this.selector = selector;
                event.consume();
                return;
            }

            if (getTool() == Tool.SELECTION)
            {
                setSelection(createSelection(positionInComponent));
                getComponent().repaint();
                this.selector = Selector.BOTTOM_RIGHT;
                event.consume();
                return;
            }
        }
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent event)
    {
        if (event.isConsumed())
        {
            return;
        }

        if (!getComponent().isSelectionAllowed())
        {
            return;
        }

        if ((event.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
        {
            if (selector != null)
            {
                final Selection selection = getSelection();

                if (selection != null && selection.updateSelection(getComponent().transfer(event.getPoint()), selector))
                {
                    getComponent().repaint();
                    setSelection(selection);
                }

                event.consume();
                return;
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent event)
    {
        if (event.isConsumed())
        {
            return;
        }

        if ((event.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
        {
            if (selector != null)
            {
                selector = null;
                event.consume();
                return;
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent event)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent event)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent event)
    {
        // intentionally left blank
    }

}
