package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.GUIAdapter;
import org.cbm.editor.font.model.events.GUIEvent;
import org.cbm.editor.font.ui.block.BlockComponent;
import org.cbm.editor.font.ui.block.droplayer.DropLayer;

public class AcceptSelectionAction extends AbstractAction
{

	private static final long serialVersionUID = 5011765810453709684L;

	private final GUIAdapter guiAdapter;

	public AcceptSelectionAction()
	{
		super("Accept Selection", Icon.ACCEPT_SELECTION.getIcon());

		guiAdapter = Registry.get(GUIAdapter.class).bind(this);

		putValue(SHORT_DESCRIPTION, "Accepts the selected layer and pastes it to the image");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ENTER"));

		updateState();
	}

	public void handleEvent(GUIEvent event)
	{
		updateState();
	}

	public void updateState()
	{
		BlockComponent component = guiAdapter.getActiveComponent();

		setEnabled((component != null) && (component.getDropLayer() != null)
				&& (component.getDropLayer().hasBlockSelection()));
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent e)
	{
		BlockComponent component = guiAdapter.getActiveComponent();
		DropLayer dropLayer = component.getDropLayer();

		switch (guiAdapter.getActiveComponentType())
		{
			case BLOCK:
				Registry.execute(new BlockAcceptEdit(component.getRootLayer().getBlock(), dropLayer.getXInCharacter(),
						dropLayer.getYInCharacter(), dropLayer.getBlockSelection()));

				dropLayer.setBlockSelection(null, false);
				break;

			case FONT:
				Registry.execute(new FontAcceptEdit(component.getRootLayer().getFont(), component.getRootLayer()
						.getBlock(), dropLayer.getXInCharacter(), dropLayer.getYInCharacter(), dropLayer
						.getBlockSelection()));

				dropLayer.setBlockSelection(null, false);
				break;

			case EDIT:
				Registry.execute(new EditAcceptEdit(component.getRootLayer().getFont(), component.getRootLayer()
						.getBlock(), dropLayer.getXInPixel(), dropLayer.getYInPixel(), dropLayer
						.getBlockSelection()));

				dropLayer.setBlockSelection(null, false);
				break;
		}

	}

}
