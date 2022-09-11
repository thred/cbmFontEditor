package org.cbm.editor.font.ui.block;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import org.cbm.editor.font.util.Objects;

public class BlockComponentTransferable implements Transferable
{

    private final Object data;

    public BlockComponentTransferable(final BlockSelection data)
    {
        super();

        this.data = data;
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]{
            BlockSelection.OBJECT_DATA_FLAVOR,
            BlockSelection.IMAGE_DATA_FLAVOR,
            BlockSelection.TEXT_DATA_FLAVOR};
    }

    /**
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor)
    {
        return Objects.equals(BlockSelection.OBJECT_DATA_FLAVOR, flavor)
            || Objects.equals(BlockSelection.IMAGE_DATA_FLAVOR, flavor)
            || Objects.equals(BlockSelection.TEXT_DATA_FLAVOR, flavor);
    }

    /**
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (Objects.equals(BlockSelection.OBJECT_DATA_FLAVOR, flavor))
        {
            return data;
        }
        else if (Objects.equals(BlockSelection.IMAGE_DATA_FLAVOR, flavor))
        {
            return ((BlockSelection) data).toImage();
        }

        return data.toString();
    }

}
