package org.cbm.editor.font.ui.edit;

import java.awt.BorderLayout;

import javax.swing.JPanel;

class EditPanel extends JPanel
{

    private static final long serialVersionUID = -7525044265060335116L;

    public EditPanel(JPanel blockPanel)
    {
        super(new BorderLayout());

        setOpaque(false);

        add(blockPanel, BorderLayout.CENTER);
    }

}
