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
import org.cbm.editor.font.ui.block.BlockComponent;

public class CopyAction extends AbstractAction implements PropertyChangeListener
{

	private static final long serialVersionUID = 2323624444536801108L;

	private JComponent component;

	public CopyAction()
	{
		super("Copy", Icon.COPY.getIcon());

		putValue(SHORT_DESCRIPTION, "Copies the selection");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl C"));

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

		if ((focusOwner instanceof JTextField) || (focusOwner instanceof BlockComponent))
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
				TransferHandler.COPY);
	}

}
