package org.cbm.editor.font.ui.action;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.ui.block.BlockComponent;

public class PasteAction extends AbstractAction implements PropertyChangeListener
{

    private static final long serialVersionUID = 2323624444536801108L;

    private JComponent component;

    public PasteAction()
    {
        super("Paste", Icon.PASTE.getIcon());

        putValue(SHORT_DESCRIPTION, "Pastes the contents of the clipboard");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl V"));

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", this);
    }

    /**
     * {@inheritDoc}
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

        if (focusOwner instanceof JTextField || focusOwner instanceof BlockComponent)
        {
            component = (JComponent) focusOwner;
        }
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if (transferable == null)
        {
            return;
        }

        component.getTransferHandler().importData(component, transferable);
    }
}
