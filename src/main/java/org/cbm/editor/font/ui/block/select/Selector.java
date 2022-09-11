package org.cbm.editor.font.ui.block.select;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;

public enum Selector
{
    TOP_LEFT(0, 0, Cursor.NW_RESIZE_CURSOR),

    TOP(0.5, 0, Cursor.N_RESIZE_CURSOR),

    TOP_RIGHT(1, 0, Cursor.NE_RESIZE_CURSOR),

    LEFT(0, 0.5, Cursor.W_RESIZE_CURSOR),

    RIGHT(1, 0.5, Cursor.E_RESIZE_CURSOR),

    BOTTOM_LEFT(0, 1, Cursor.SW_RESIZE_CURSOR),

    BOTTOM(0.5, 1, Cursor.S_RESIZE_CURSOR),

    BOTTOM_RIGHT(1, 1, Cursor.SE_RESIZE_CURSOR);

    private final double relativeX;
    private final double relativeY;
    private final int cursor;

    private Selector(final double relativeX, final double relativeY, final int cursor)
    {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.cursor = cursor;
    }

    public double getRelativeX()
    {
        return relativeX;
    }

    public double getRelativeY()
    {
        return relativeY;
    }

    public int getCursor()
    {
        return cursor;
    }

    public Point getPosition(final Rectangle r)
    {
        return new Point((int) (r.x + r.width * relativeX), (int) (r.y + r.height * relativeY));
    }

}
