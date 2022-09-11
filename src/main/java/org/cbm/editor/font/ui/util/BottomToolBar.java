package org.cbm.editor.font.ui.util;

import java.awt.Graphics;

import javax.swing.JToolBar;

public class BottomToolBar extends JToolBar
{

    private static final long serialVersionUID = 8367972638342566389L;

    public BottomToolBar()
    {
        super();
    }

    public BottomToolBar(int orientation)
    {
        super(orientation);
    }

    public BottomToolBar(String name, int orientation)
    {
        super(name, orientation);
    }

    public BottomToolBar(String name)
    {
        super(name);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics graphics)
    {
        Colors.backgroundBottom(graphics, getBackground(), getWidth(), getHeight());

        super.paintComponent(graphics);
    }

}
