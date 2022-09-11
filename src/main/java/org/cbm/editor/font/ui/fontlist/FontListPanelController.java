package org.cbm.editor.font.ui.fontlist;

import javax.swing.JPanel;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.block.BlockPanelController;
import org.cbm.editor.font.ui.block.blocklayer.FontBlockLayer;
import org.cbm.editor.font.ui.block.droplayer.FontDropLayer;
import org.cbm.editor.font.ui.block.highlight.FontHighlightLayer;
import org.cbm.editor.font.ui.block.select.SelectionLayer;

public class FontListPanelController
{

    private final ProjectAdapter projectAdapter;
    private final FontAdapter fontAdapter;
    private final BlockPanelController blockPanelController;
    private final FontListPanel view;

    public FontListPanelController()
    {
        super();

        projectAdapter = Registry.get(ProjectAdapter.class);
        projectAdapter.bind(this);

        fontAdapter = Registry.get(FontAdapter.class);
        fontAdapter.bind(this);

        blockPanelController = new BlockPanelController();
        blockPanelController.setHighlightLayer(new FontHighlightLayer());
        blockPanelController.setDropLayer(new FontDropLayer());
        blockPanelController.setSelectionLayer(new SelectionLayer(8));
        blockPanelController.setRootLayer(new FontBlockLayer());
        blockPanelController.setPopupMenu(Registry.get(FontListMenu.class));

        view = new FontListPanel(blockPanelController.getView());
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
