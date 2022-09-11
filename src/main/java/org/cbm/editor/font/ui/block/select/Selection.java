package org.cbm.editor.font.ui.block.select;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;

import org.cbm.editor.font.Icon;

public class Selection
{

    private static final Stroke SELECTION_STROKE =
        new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{4, 4}, 0);
    private static final Stroke SELECTION_BACKGROUND_STROKE =
        new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    private final SelectionLayer layer;
    private final int steps;

    private Rectangle rectangle;

    public Selection(final SelectionLayer layer, final Point positionInComponent, int steps)
    {
        super();

        this.layer = layer;
        this.steps = steps;

        final Point location = convertFromComponentToPixel(layer, positionInComponent);

        location.x = roundDown(location.x);
        location.y = roundDown(location.y);

        rectangle = new Rectangle(location.x, location.y, steps, steps);
    }

    protected Point convertFromComponentToPixel(SelectionLayer layer, Point pointInComponent)
    {
        return layer.getComponent().convertFromComponentToPixel(pointInComponent);
    }

    protected Rectangle convertFromPixelToComponent(final SelectionLayer layer, final Rectangle selection)
    {
        final Point point = layer.getComponent().convertFromPixelToComponent(selection.getLocation());

        point.x -= 1;
        point.y -= 1;

        return new Rectangle(point, new Dimension((int) (selection.getWidth() * layer.getComponent().getZoom()) + 1,
            (int) (selection.getHeight() * layer.getComponent().getZoom()) + 1));
    }

    //	protected abstract Rectangle convertSelectionToBlock(AbstractSelectionLayer layer, Rectangle selection);

    protected Rectangle convertFromPixelToCharacter(SelectionLayer layer, Rectangle rectangleInPixel)
    {
        return layer.getComponent().convertFromPixelToCharacter(rectangleInPixel);
    }

    protected int roundUp(int value)
    {
        return value >= 0 ? (value + steps) / steps * steps : value / steps * steps;
    }

    protected int roundDown(int value)
    {
        return value >= 0 ? value / steps * steps : (value - steps) / steps * steps;
    }

    public SelectionLayer getLayer()
    {
        return layer;
    }

    public boolean contains(final Point point)
    {
        return rectangle.contains(point);
    }

    public boolean contains(int x, int y)
    {
        return rectangle.contains(x, y);
    }

    public Rectangle getRectangle()
    {
        return rectangle;
    }

    public Rectangle getRectangleForComponent()
    {
        return convertFromPixelToComponent(layer, rectangle);
    }

    //	public Rectangle getRectangleForBlock()
    //	{
    //		return convertSelectionToBlock(layer, selection);
    //	}

    public Rectangle getRectangleForCharacters()
    {
        return convertFromPixelToCharacter(layer, rectangle);
    }

    public Selector getSelector(final Point positionInComponent)
    {
        final Rectangle r = convertFromPixelToComponent(layer, rectangle);

        for (final Selector selector : Selector.values())
        {
            if (isInSelector(positionInComponent, r, selector))
            {
                return selector;
            }
        }

        return null;
    }

    public Cursor getCursor(final Point point)
    {
        final Selector selector = getSelector(point);

        if (selector != null)
        {
            return Cursor.getPredefinedCursor(selector.getCursor());
        }

        return null;
    }

    private boolean isInSelector(final Point positionInComponent, final Rectangle r, final Selector selector)
    {
        final Point selectorPoint = selector.getPosition(r);

        return positionInComponent.x >= selectorPoint.x - 5
            && positionInComponent.y >= selectorPoint.y - 5
            && positionInComponent.x <= selectorPoint.x + 5
            && positionInComponent.y <= selectorPoint.y + 5;
    }

    private boolean paintSelector(final Graphics2D g, final Rectangle r, final Selector selector)
    {
        final Point point = selector.getPosition(r);

        return g.drawImage(Icon.SELECTOR.getImage(), point.x - 5, point.y - 5, null);
    }

    public boolean updateSelection(final Point positionInComponent, final Selector selector)
    {
        final Point positionInPixel = convertFromComponentToPixel(layer, positionInComponent);

        int x0 = rectangle.x;
        int y0 = rectangle.y;
        int x1 = rectangle.x + rectangle.width;
        int y1 = rectangle.y + rectangle.height;

        switch (selector)
        {
            case TOP_LEFT:
                x0 = roundDown(positionInPixel.x);
                y0 = roundDown(positionInPixel.y);
                break;

            case TOP:
                y0 = roundDown(positionInPixel.y);
                break;

            case TOP_RIGHT:
                x1 = roundUp(positionInPixel.x);
                y0 = roundDown(positionInPixel.y);
                break;

            case LEFT:
                x0 = roundDown(positionInPixel.x);
                break;

            case RIGHT:
                x1 = roundUp(positionInPixel.x);
                break;

            case BOTTOM_LEFT:
                x0 = roundDown(positionInPixel.x);
                y1 = roundUp(positionInPixel.y);
                break;

            case BOTTOM:
                y1 = roundUp(positionInPixel.y);
                break;

            case BOTTOM_RIGHT:
                x1 = roundUp(positionInPixel.x);
                y1 = roundUp(positionInPixel.y);
                break;
        }

        final Rectangle selection = new Rectangle(x0, y0, Math.max(x1 - x0, steps), Math.max(y1 - y0, steps));

        if (selection.equals(rectangle))
        {
            return false;
        }

        rectangle = selection;

        return true;
    }

    public void draw(final Graphics2D g)
    {
        final Rectangle r = convertFromPixelToComponent(layer, rectangle);

        g.setStroke(SELECTION_BACKGROUND_STROKE);
        g.setColor(Color.BLACK);
        g.draw(r);
        g.setStroke(SELECTION_STROKE);
        g.setColor(Color.WHITE);
        g.draw(r);

        for (final Selector selector : Selector.values())
        {
            paintSelector(g, r, selector);
        }
    }

}
