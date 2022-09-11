package org.cbm.editor.font.ui.util;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class TitledPanel extends JPanel
{

    private static final long serialVersionUID = -7326651505400559076L;

    private final TitleComponent titleComponent;

    public TitledPanel(final String title, final String subtitle, final JComponent component,
        final JComponent... footerComponents)
    {
        super();

        setLayout(new BorderLayout());

        titleComponent = new TitleComponent(title, subtitle);

        add(titleComponent, BorderLayout.NORTH);
        add(component, BorderLayout.CENTER);

        if (footerComponents != null && footerComponents.length > 0)
        {
            add(new FooterComponent(footerComponents), BorderLayout.SOUTH);
        }
    }

    public void setFocusForComponent(final JComponent showFocusOfComponent)
    {
        titleComponent.setFocusForComponent(showFocusOfComponent);
    }

}
