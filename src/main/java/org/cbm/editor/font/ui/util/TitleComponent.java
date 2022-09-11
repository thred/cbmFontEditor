package org.cbm.editor.font.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class TitleComponent extends JPanel implements FocusListener, MouseListener
{

    private static final long serialVersionUID = -7604783472008446430L;

    private final JLabel titleLabel;
    private final JLabel subtitleLabel;

    private JComponent focusForComponent;

    public TitleComponent(final String title, final String subtitle)
    {
        super(new BorderLayout());

        setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        titleLabel = new JLabel(title != null ? title : "");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, titleLabel.getFont().getSize() * 1.8f));

        subtitleLabel = new JLabel(subtitle != null ? subtitle : "");

        addMouseListener(this);

        add(titleLabel, BorderLayout.CENTER);
        add(subtitleLabel, BorderLayout.EAST);
    }

    public void setTitle(final String title, final String subtitle)
    {
        titleLabel.setText(title);
        subtitleLabel.setText(subtitle);
    }

    public JComponent getFocusForComponent()
    {
        return focusForComponent;
    }

    public void setFocusForComponent(final JComponent showFocusOfComponent)
    {
        if (focusForComponent != null)
        {
            focusForComponent.removeFocusListener(this);
        }

        focusForComponent = showFocusOfComponent;

        if (showFocusOfComponent != null)
        {
            showFocusOfComponent.addFocusListener(this);
        }
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics graphics)
    {
        Color color = getBackground();
        Color focusColor = (Color) UIManager.getLookAndFeelDefaults().get("nimbusFocus");

        focusColor = new Color(0xa6bbce);

        if (focusColor == null || focusForComponent != null && !focusForComponent.hasFocus())
        {
            focusColor = color;
        }

        Colors.backgroundLowered(graphics, focusColor, color, getWidth(), getHeight());

        super.paintComponent(graphics);
    }

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    @Override
    public void focusGained(final FocusEvent e)
    {
        repaint();
    }

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    @Override
    public void focusLost(final FocusEvent e)
    {
        repaint();
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e)
    {
        // intentionally left blank
        if (focusForComponent != null)
        {
            if (!focusForComponent.hasFocus())
            {
                focusForComponent.grabFocus();
            }
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e)
    {
        // intentionally left blank
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e)
    {
        // intentionally left blank
    }

}
