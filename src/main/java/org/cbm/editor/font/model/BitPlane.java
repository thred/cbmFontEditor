package org.cbm.editor.font.model;

import java.util.Arrays;

public class BitPlane
{

    private final int width;
    private final int height;
    private final boolean[] bits;

    public BitPlane(final int width, final int height)
    {
        super();

        this.width = width;
        this.height = height;

        bits = new boolean[width * height];

        Arrays.fill(bits, false);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean is(final int x, final int y)
    {
        defenseXY(x, y);

        return bits[x + y * width];
    }

    public BitPlane set(final int x, final int y, final boolean bit)
    {
        defenseXY(x, y);

        bits[x + y * width] = bit;

        return this;
    }

    public boolean isXYInBounds(final int x, final int y)
    {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    public BitPlane defenseXY(final int x, final int y)
    {
        if (!isXYInBounds(x, y))
        {
            throw new IllegalArgumentException(String.format("Position out of bounds: %d, %d", x, y));
        }

        return this;
    }

}
