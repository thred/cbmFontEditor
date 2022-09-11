package org.cbm.editor.font.ui.util;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Constraints extends GridBagConstraints
{

    private static final long serialVersionUID = 1L;

    public Constraints()
    {
        super();

        gridx = 1;
        gridy = 1;
        gridwidth = 1;
        gridheight = 1;
        anchor = GridBagConstraints.WEST;

        insets = new Insets(2, 4, 2, 4);
    }

    public Constraints reset()
    {
        fill = GridBagConstraints.NONE;
        anchor = GridBagConstraints.WEST;
        weightx = 0;
        weighty = 0;
        gridwidth = 1;
        gridheight = 1;

        return this;
    }

    public Constraints x(final int x)
    {
        gridx = x;

        return reset();
    }

    public Constraints next()
    {
        return x(gridx + gridwidth);
    }

    public Constraints y(final int y)
    {
        gridy = y;

        return reset();
    }

    public Constraints nextLine()
    {
        gridx = 1;

        return y(gridy + gridheight);
    }

    public Constraints width(final int width)
    {
        gridwidth = width;

        return this;
    }

    public Constraints height(final int height)
    {
        gridheight = height;

        return this;
    }

    public Constraints right()
    {
        anchor = GridBagConstraints.EAST;

        return this;
    }

    public Constraints fill()
    {
        fill = GridBagConstraints.BOTH;

        return this;
    }

    public Constraints fillHorizontal()
    {
        fill = GridBagConstraints.HORIZONTAL;

        return this;
    }

    public Constraints fillVertical()
    {
        fill = GridBagConstraints.VERTICAL;

        return this;
    }

    public Constraints alignLeft()
    {
        anchor = GridBagConstraints.WEST;

        return this;
    }

    public Constraints alignRight()
    {
        anchor = GridBagConstraints.EAST;

        return this;
    }

    public Constraints weight(final double weightX)
    {
        weightx = weightX;

        return this;
    }

    public Constraints weight(final double weightX, final double weightY)
    {
        weightx = weightX;
        weighty = weightY;

        return this;
    }

}
