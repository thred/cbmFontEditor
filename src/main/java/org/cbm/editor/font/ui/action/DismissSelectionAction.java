package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.events.GUIEvent;
import org.cbm.editor.font.ui.block.BlockComponent;

public class DismissSelectionAction extends AbstractAction
{

    private static final long serialVersionUID = 5011765810453709684L;

    private final GUIAdapter guiAdapter;

    public DismissSelectionAction()
    {
        super("Dismiss Selection", Icon.DISMISS_SELECTION.getIcon());

        guiAdapter = Registry.get(GUIAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Dismisses the selection");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ESCAPE"));

        updateState();
    }

    public void handleEvent(GUIEvent event)
    {
        updateState();
    }

    public void updateState()
    {
        BlockComponent component = guiAdapter.getActiveComponent();

        setEnabled(component != null
            && (component.getSelectionLayer().getSelection() != null
                || component.getDropLayer() != null && component.getDropLayer().hasBlockSelection()));
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        guiAdapter.getActiveComponent().getSelectionLayer().clearSelection();

        if (guiAdapter.getActiveComponent().getDropLayer() != null)
        {
            guiAdapter.getActiveComponent().getDropLayer().setBlockSelection(null, false);
        }
    }

    //	public AbstractBlockComponent getComponent()
    //	{
    //		final ApplicationState applicationState = Registry.get(ApplicationState.class);
    //		final JComponent focussedComponent = applicationState.getFocussedComponent();
    //
    //		if (focussedComponent == null)
    //		{
    //			return null;
    //		}
    //
    //		if (!(focussedComponent instanceof AbstractBlockComponent))
    //		{
    //			return null;
    //		}
    //
    //		return (AbstractBlockComponent) focussedComponent;
    //	}

}
