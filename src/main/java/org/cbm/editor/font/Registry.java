package org.cbm.editor.font;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.undo.UndoManager;

import org.cbm.editor.font.ui.action.AbstractEdit;
import org.cbm.editor.font.ui.action.RedoAction;
import org.cbm.editor.font.ui.action.UndoAction;

public class Registry
{

    protected static final Map<Class<?>, Object> GLOBALS = new HashMap<>();

    protected static final UndoManager UNDO_MANAGER = new UndoManager();

    public static UndoManager getUndoManager()
    {
        return UNDO_MANAGER;
    }

    public static void execute(final AbstractEdit edit)
    {
        SwingUtilities.invokeLater(() -> {
            edit.execute();
            alreadyExecuted(edit);
        });
    }

    public static void alreadyExecuted(final AbstractEdit edit)
    {
        UNDO_MANAGER.addEdit(edit);
        get(UndoAction.class).updateState();
        get(RedoAction.class).updateState();
    }

    public static <TYPE> TYPE get(final Class<TYPE> type)
    {
        synchronized (GLOBALS)
        {
            @SuppressWarnings("unchecked")
            TYPE global = (TYPE) GLOBALS.get(type);

            if (global == null)
            {
                try
                {
                    global = type.newInstance();
                }
                catch (final InstantiationException e)
                {
                    throw new IllegalArgumentException("Failed to instantiate " + type, e);
                }
                catch (final IllegalAccessException e)
                {
                    throw new IllegalArgumentException("Failed to access " + type, e);
                }

                GLOBALS.put(type, global);
            }

            return global;
        }
    }
}
