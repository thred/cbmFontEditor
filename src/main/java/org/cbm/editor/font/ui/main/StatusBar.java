package org.cbm.editor.font.ui.main;

import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.cbm.editor.font.ui.util.Constraints;

class StatusBar extends JPanel
{

    private static final long serialVersionUID = 1402580171209136745L;

    private final JLabel messageLabel;
    private final JLabel fileLabel;

    public StatusBar()
    {
        super(new GridBagLayout());

        setOpaque(false);

        messageLabel = new JLabel();
        fileLabel = new JLabel();
        fileLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        final Constraints c = new Constraints();

        add(messageLabel, c.weight(1).fillHorizontal());
        add(new JSeparator(), c.next());
        add(fileLabel, c.next().fillHorizontal());
    }

    public void setMessage(String message)
    {
        messageLabel.setText(message);
    }

    public void setFile(String file)
    {
        fileLabel.setText(file);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics graphics)
    {
        //		final Color bg = getBackground();
        //		final Paint paint = new LinearGradientPaint(0, 0, 0, getHeight(), new float[] {
        //				0f, 0.05f, 0.50f, 0.90f, 1f
        //		}, new Color[] {
        //			bg.brighter(), bg.darker(), bg, bg.brighter(), bg.darker()
        //		});
        //		final Color bg = getBackground();
        //		final Paint paint = new LinearGradientPaint(0, 0, 0, getHeight(), new float[] {
        //				0f, 0.03f, 0.50f, 1f
        //		}, new Color[] {
        //				Colors.brighter(bg,  0.1f), Colors.darker(bg, 0.9f), bg, bg
        //		});
        //
        //		final Graphics2D g = (Graphics2D) graphics;
        //
        //		g.setPaint(paint);
        //		g.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(graphics);
    }

}
