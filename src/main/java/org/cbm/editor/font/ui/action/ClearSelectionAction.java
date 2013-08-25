package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.GUIComponentType;
import org.cbm.editor.font.model.events.GUIEvent;
import org.cbm.editor.font.ui.block.BlockComponent;

public class ClearSelectionAction extends AbstractAction
{

	private static final long serialVersionUID = 5011765810453709684L;

	private final GUIAdapter guiAdapter;

	public ClearSelectionAction()
	{
		super("Clear Selection", Icon.CLEAR_SELECTION.getIcon());

		guiAdapter = Registry.get(GUIAdapter.class).bind(this);

		putValue(SHORT_DESCRIPTION, "Clears the selected area");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));

		updateState();
	}

	public void handleEvent(GUIEvent event)
	{
		updateState();
	}

	public void updateState()
	{
		BlockComponent component = guiAdapter.getActiveComponent();

		setEnabled((component != null) && (guiAdapter.getActiveComponentType() != GUIComponentType.FONT)
				&& (component.getSelectionLayer().getSelection() != null));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		BlockComponent blockComponent = guiAdapter.getActiveComponent();

		switch (guiAdapter.getActiveComponentType())
		{
			case BLOCK:
				Registry.execute(new ClearBlockCharactersEdit(blockComponent.getRootLayer().getBlock(), blockComponent
						.getSelectionLayer().getSelection().getRectangleForCharacters()));
				break;

			case EDIT:
				Registry.execute(new ClearBlockPixelsEdit(blockComponent.getRootLayer().getFont(), blockComponent
						.getRootLayer().getBlock(), blockComponent.getSelectionLayer().getSelection().getRectangle()));
				break;
		}
	}

}
