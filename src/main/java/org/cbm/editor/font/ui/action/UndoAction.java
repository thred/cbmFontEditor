package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotUndoException;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;

public class UndoAction extends AbstractAction
{

	private static final long serialVersionUID = -3048068249874818278L;

	public UndoAction()
	{
		super("Undo", Icon.UNDO.getIcon());

		setEnabled(false);

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Z"));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent event)
	{
		try
		{
			Registry.getUndoManager().undo();
		}
		catch (final CannotUndoException e)
		{
			e.printStackTrace(System.err);
		}

		updateState();
		Registry.get(RedoAction.class).updateState();
	}

	public void updateState()
	{
		if (Registry.getUndoManager().canUndo())
		{
			setEnabled(true);
			putValue(SHORT_DESCRIPTION, Registry.getUndoManager().getUndoPresentationName());
		}
		else
		{
			setEnabled(false);
			putValue(SHORT_DESCRIPTION, null);
		}
	}
}