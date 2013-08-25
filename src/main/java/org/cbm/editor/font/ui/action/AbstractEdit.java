package org.cbm.editor.font.ui.action;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public abstract class AbstractEdit extends AbstractUndoableEdit
{

	/**
     * 
     */
	private static final long serialVersionUID = -8592329576374242124L;
	private final String presentationName;

	public AbstractEdit(final String presentationName)
	{
		super();

		this.presentationName = presentationName;
	}

	/**
	 * @see javax.swing.undo.AbstractUndoableEdit#getPresentationName()
	 */
	@Override
	public final String getPresentationName()
	{
		return presentationName;
	}

	public abstract void execute();

	public abstract void rollback();

	/**
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	@Override
	public final void redo() throws CannotRedoException
	{
		super.redo();

		execute();
	}

	/**
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	@Override
	public final void undo() throws CannotUndoException
	{
		super.undo();

		rollback();
	}

}
