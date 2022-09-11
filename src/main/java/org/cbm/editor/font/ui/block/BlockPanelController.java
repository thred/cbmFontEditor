package org.cbm.editor.font.ui.block;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.cbm.editor.font.ui.block.blocklayer.BlockLayer;
import org.cbm.editor.font.ui.block.droplayer.DropLayer;
import org.cbm.editor.font.ui.block.highlight.HighlightLayer;
import org.cbm.editor.font.ui.block.select.SelectionLayer;

public class BlockPanelController
{

    private final BlockPanel view;

    public BlockPanelController()
    {
        super();

        view = new BlockPanel();
    }

    public JPanel getView()
    {
        return view;
    }

    public void setRootLayer(BlockLayer layer)
    {
        view.setRootLayer(layer);
    }

    public void setSelectionLayer(SelectionLayer layer)
    {
        view.setSelectionLayer(layer);
    }

    public void setDropLayer(DropLayer layer)
    {
        view.setDropLayer(layer);
    }

    public void setHighlightLayer(HighlightLayer layer)
    {
        view.setHighlightLayer(layer);
    }

    public void setPopupMenu(JPopupMenu popupMenu)
    {
        view.setPopupMenu(popupMenu);
    }

    public BlockComponent getBlockComponent()
    {
        return view.getBlockComponent();
    }

}
