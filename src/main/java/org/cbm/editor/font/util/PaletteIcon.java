package org.cbm.editor.font.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class PaletteIcon implements Icon
{

    private final Color color;

    public PaletteIcon(Color color)
    {
        super();

        this.color = color;
    }

    /**
     * @see javax.swing.Icon#getIconWidth()
     */
    @Override
    public int getIconWidth()
    {
        return 24;
    }

    /**
     * @see javax.swing.Icon#getIconHeight()
     */
    @Override
    public int getIconHeight()
    {
        return 24;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        g.setColor(color);
        g.fillOval(x + 3, y + 3, 18, 18);
        g.drawImage(org.cbm.editor.font.Icon.COLOR.getImage(), x, y, null);
    }

}
