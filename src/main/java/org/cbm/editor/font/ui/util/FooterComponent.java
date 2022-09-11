package org.cbm.editor.font.ui.util;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class FooterComponent extends JPanel
{

    private static final long serialVersionUID = -847719239904727525L;

    public FooterComponent(final JComponent... compoents)
    {
        super(new BorderLayout());

        setOpaque(false);

        final JPanel panel = new JPanel(new GridBagLayout());

        panel.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        panel.setOpaque(false);

        final Constraints c = new Constraints();

        panel.add(new JLabel(), c.weight(1));

        for (final JComponent component : compoents)
        {
            panel.add(component, c.next());
        }

        add(panel, BorderLayout.CENTER);
        add(new JSeparator(), BorderLayout.NORTH);
    }

}
