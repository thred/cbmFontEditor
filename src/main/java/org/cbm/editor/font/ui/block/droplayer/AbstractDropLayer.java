package org.cbm.editor.font.ui.block.droplayer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.cbm.editor.font.CBMCursor;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.Tool;
import org.cbm.editor.font.model.events.GUISelectionEvent;
import org.cbm.editor.font.ui.action.AcceptSelectionAction;
import org.cbm.editor.font.ui.block.AbstractLayer;
import org.cbm.editor.font.ui.block.BlockComponent;
import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.util.Events;

public abstract class AbstractDropLayer extends AbstractLayer implements DropLayer, MouseListener, MouseMotionListener
{

    private static final Stroke DROP_STROKE =
        new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{4, 4}, 0);
    private static final Stroke DROP_BACKGROUND_STROKE =
        new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private final int steps;

    private BlockSelection blockSelection;
    private int xInPixel;
    private int yInPixel;

    private Point referenceInPixel;

    public AbstractDropLayer(int steps)
    {
        super();

        this.steps = steps;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#getBlockSelection()
     */
    @Override
    public BlockSelection getBlockSelection()
    {
        return blockSelection;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#setBlockSelection(org.cbm.editor.font.ui.block.BlockSelection,
     *      boolean)
     */
    @Override
    public void setBlockSelection(BlockSelection blockSelection, boolean center)
    {
        if (this.blockSelection != blockSelection)
        {
            this.blockSelection = blockSelection;

            BlockComponent component = getComponent();
            if (blockSelection != null)
            {
                component.getSelectionLayer().clearSelection();

                if (center)
                {
                    Rectangle visibleRectangle = new Rectangle();

                    component.computeVisibleRect(visibleRectangle);

                    Point visibleCenterInPixel = component.convertFromComponentToPixel(
                        component.transfer(new Point(visibleRectangle.x + visibleRectangle.width / 2,
                            visibleRectangle.y + visibleRectangle.height / 2)));

                    visibleCenterInPixel.x -= blockSelection.getBlock().getWidth() / 2;
                    visibleCenterInPixel.y -= blockSelection.getBlock().getHeight() / 2;

                    setXInPixel(visibleCenterInPixel.x);
                    setYInPixel(visibleCenterInPixel.y);
                }
            }

            Events.fire(Registry.get(GUIAdapter.class), new GUISelectionEvent());
            component.repaint();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#hasBlockSelection()
     */
    @Override
    public boolean hasBlockSelection()
    {
        return getBlockSelection() != null;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#getXInPixel()
     */
    @Override
    public int getXInPixel()
    {
        return xInPixel;
    }

    public void setXInPixel(int xInPixel)
    {
        this.xInPixel = round(xInPixel);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#getYInPixel()
     */
    @Override
    public int getYInPixel()
    {
        return yInPixel;
    }

    public void setYInPixel(int yInPixel)
    {
        this.yInPixel = round(yInPixel);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#getXInCharacter()
     */
    @Override
    public int getXInCharacter()
    {
        return (int) Math.floor(getXInPixel() / 8);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#getYInCharacter()
     */
    @Override
    public int getYInCharacter()
    {
        return (int) Math.floor(getYInPixel() / 8);
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#getWidthInPixel()
     */
    @Override
    public int getWidthInPixel()
    {
        BlockSelection blockSelection = getBlockSelection();

        return blockSelection != null ? blockSelection.getRectangle().width : 0;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.droplayer.DropLayer#getHeightInPixel()
     */
    @Override
    public int getHeightInPixel()
    {
        BlockSelection blockSelection = getBlockSelection();

        return blockSelection != null ? blockSelection.getRectangle().height : 0;
    }

    @Override
    public Rectangle getRectangleInPixel()
    {
        return new Rectangle(getXInPixel(), getYInPixel(), getWidthInPixel(), getHeightInPixel());
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.ui.block.Layer#getCursor(java.awt.Point, org.cbm.editor.font.model.Tool)
     */
    @Override
    public Cursor getCursor(Point positionInComponent, Tool tool)
    {
        if (blockSelection == null)
        {
            return null;
        }

        if (getRectangleInPixel().contains(getComponent().convertFromComponentToPixel(positionInComponent)))
        {
            return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        }

        return CBMCursor.SELECTION_ACCEPT.getCursor();
    }

    @Override
    public void draw(Graphics2D g)
    {
        final Rectangle r = getComponent().convertFromPixelToComponent(getRectangleInPixel());

        g.setStroke(DROP_BACKGROUND_STROKE);
        g.setColor(Color.BLACK);
        g.draw(r);
        g.setStroke(DROP_STROKE);
        g.setColor(Color.GREEN);
        g.draw(r);
    }

    protected int round(int value)
    {
        value = value + steps / 2;

        return value >= 0 ? value / steps * steps : (value - steps) / steps * steps;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        if (e.isConsumed())
        {
            return;
        }

        if (blockSelection == null)
        {
            return;
        }

        Point positionInPixel = getComponent().convertFromComponentToPixel(getComponent().transfer(e.getPoint()));
        Rectangle rectangleInPixel = getRectangleInPixel();

        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
        {
            if (rectangleInPixel.contains(positionInPixel))
            {
                referenceInPixel =
                    new Point(positionInPixel.x - rectangleInPixel.x, positionInPixel.y - rectangleInPixel.y);
                e.consume();
            }
            else
            {
                Registry.get(AcceptSelectionAction.class).actionPerformed(
                    new ActionEvent(getComponent(), ActionEvent.ACTION_PERFORMED, null));
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (e.isConsumed())
        {
            return;
        }

        referenceInPixel = null;
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (e.isConsumed())
        {
            return;
        }

        Point positionInPixel = getComponent().convertFromComponentToPixel(getComponent().transfer(e.getPoint()));

        if (referenceInPixel != null)
        {
            setXInPixel(positionInPixel.x - referenceInPixel.x);
            setYInPixel(positionInPixel.y - referenceInPixel.y);

            getComponent().repaint();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        // intentionally left blank
    }

}
