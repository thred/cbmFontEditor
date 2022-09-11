package org.cbm.editor.font.ui.block.droplayer;

import java.awt.Rectangle;

import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.ui.block.Layer;

public interface DropLayer extends Layer
{

    public BlockSelection getBlockSelection();

    public void setBlockSelection(BlockSelection blockSelection, boolean center);

    public boolean hasBlockSelection();

    public int getXInPixel();

    public int getYInPixel();

    public int getXInCharacter();

    public int getYInCharacter();

    public int getWidthInPixel();

    public int getHeightInPixel();

    public Rectangle getRectangleInPixel();

}
