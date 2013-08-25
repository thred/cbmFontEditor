package org.cbm.editor.font.ui.edit;

import javax.swing.JPanel;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.ui.block.BlockPanelController;
import org.cbm.editor.font.ui.block.blocklayer.EditBlockLayer;
import org.cbm.editor.font.ui.block.droplayer.EditDropLayer;
import org.cbm.editor.font.ui.block.highlight.BlockHighlightLayer;
import org.cbm.editor.font.ui.block.highlight.EditHighlightLayer;
import org.cbm.editor.font.ui.block.select.SelectionLayer;

public class EditPanelController
{

	private final BlockPanelController blockPanelController;
	private final EditPanel view;

	public EditPanelController()
	{
		super();

		blockPanelController = new BlockPanelController();
		blockPanelController.setHighlightLayer(new EditHighlightLayer());
		blockPanelController.setDropLayer(new EditDropLayer());
		blockPanelController.setSelectionLayer(new SelectionLayer(1));
		blockPanelController.setRootLayer(new EditBlockLayer());
		blockPanelController.setPopupMenu(Registry.get(EditMenu.class));

		view = new EditPanel(blockPanelController.getView());
	}

	public JPanel getView()
	{
		return view;
	}

	public BlockPanelController getBlockPanelController()
	{
		return blockPanelController;
	}

}
