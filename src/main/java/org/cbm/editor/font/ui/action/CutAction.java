package org.cbm.editor.font.ui.action;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.ui.block.BlockComponent;

public class CutAction extends AbstractAction implements PropertyChangeListener
{

    private static final long serialVersionUID = 2323624444536801108L;

    private JComponent component;

    public CutAction()
    {
        super("Cut", Icon.CUT.getIcon());

        putValue(SHORT_DESCRIPTION, "Copies and clears the selected area");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl X"));

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
    public void actionPerformed(ActionEvent e)
    {
        component.getTransferHandler().exportToClipboard(component, Toolkit.getDefaultToolkit().getSystemClipboard(),
            TransferHandler.MOVE);

        if (component instanceof BlockComponent)
        {
            BlockComponent blockComponent = (BlockComponent) component;

            if (blockComponent == Registry.get(GUIAdapter.class).getBlockComponent())
            {
                Registry.execute(new ClearBlockCharactersEdit(blockComponent.getRootLayer().getBlock(),
                    blockComponent.getSelectionLayer().getSelection().getRectangleForCharacters()));
            }
            else if (blockComponent == Registry.get(GUIAdapter.class).getEditComponent())
            {
                Registry.execute(new ClearBlockPixelsEdit(blockComponent.getRootLayer().getFont(),
                    blockComponent.getRootLayer().getBlock(),
                    blockComponent.getSelectionLayer().getSelection().getRectangle()));
            }
        }
    }

}
