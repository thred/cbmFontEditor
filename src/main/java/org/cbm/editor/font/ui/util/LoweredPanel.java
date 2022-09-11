package org.cbm.editor.font.ui.util;

import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class LoweredPanel extends JPanel
{

    private static final long serialVersionUID = 4165457219038469042L;

    public LoweredPanel()
    {
        super();

        setOpaque(false);
    }

    public LoweredPanel(boolean isDoubleBuffered)
    {
        super(isDoubleBuffered);

        setOpaque(false);
    }

    public LoweredPanel(LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);

        setOpaque(false);
    }

    public LoweredPanel(LayoutManager layout)
    {
        super(layout);

        setOpaque(false);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics graphics)
    {
        Colors.backgroundLowered(graphics, getBackground(), getWidth(), getHeight());

        super.paintComponent(graphics);
    }

}
