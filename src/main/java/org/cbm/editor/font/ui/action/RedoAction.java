package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotRedoException;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;

public class RedoAction extends AbstractAction
{

	private static final long serialVersionUID = -4690749336183371897L;

	public RedoAction()
	{
		super("Redo", Icon.REDO.getIcon());

		setEnabled(false);

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Y"));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent event)
	{
		try
		{
			Registry.getUndoManager().redo();
		}
		catch (final CannotRedoException e)
		{
			e.printStackTrace(System.err);
		}

		updateState();
		Registry.get(UndoAction.class).updateState();
	}

	public void updateState()
	{
		if (Registry.getUndoManager().canRedo())
		{
			setEnabled(true);
			putValue(SHORT_DESCRIPTION, Registry.getUndoManager().getRedoPresentationName());
		}
		else
		{
			setEnabled(false);
			putValue(SHORT_DESCRIPTION, null);
		}
	}
}
