package org.cbm.editor.font.ui.blocklist;

import javax.swing.JPanel;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.block.BlockPanelController;
import org.cbm.editor.font.ui.block.blocklayer.BlockBlockLayer;
import org.cbm.editor.font.ui.block.droplayer.BlockDropLayer;
import org.cbm.editor.font.ui.block.highlight.AbstractHighlightLayer;
import org.cbm.editor.font.ui.block.highlight.BlockHighlightLayer;
import org.cbm.editor.font.ui.block.select.SelectionLayer;

public class BlockListPanelController
{

	private final ProjectAdapter projectAdapter;
	private final BlockAdapter blockAdapter;
	private final BlockPanelController blockPanelController;
	private final BlockListPanel view;

	public BlockListPanelController()
	{
		super();

		projectAdapter = Registry.get(ProjectAdapter.class);
		projectAdapter.bind(this);

		blockAdapter = Registry.get(BlockAdapter.class);
		blockAdapter.bind(this);

		blockPanelController = new BlockPanelController();
		blockPanelController.setHighlightLayer(new BlockHighlightLayer());
		blockPanelController.setDropLayer(new BlockDropLayer());
		blockPanelController.setSelectionLayer(new SelectionLayer(8));
		blockPanelController.setRootLayer(new BlockBlockLayer());
		blockPanelController.setPopupMenu(Registry.get(BlockListMenu.class));

		view = new BlockListPanel(blockPanelController.getView());
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
