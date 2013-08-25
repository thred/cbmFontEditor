package org.cbm.editor.font.ui.block;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.cbm.editor.font.ui.block.droplayer.DropLayer;

public class BlockComponentTransferHandler extends TransferHandler
{

	private static final long serialVersionUID = -350165703837050414L;

	public BlockComponentTransferHandler()
	{
		super();
	}

	/**
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	@Override
	public int getSourceActions(final JComponent component)
	{
		return COPY | MOVE;
	}

	/**
	 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	@Override
	protected Transferable createTransferable(final JComponent c)
	{
		final BlockSelection blockSelection = ((BlockComponent) c).createBlockSelection();

		if (blockSelection == null)
		{
			return null;
		}

		return new BlockComponentTransferable(blockSelection);
	}

	/**
	 * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
	 */
	@Override
	public boolean canImport(final TransferSupport support)
	{
		if (!support.isDrop())
		{
			return false;
		}

		if (!support.isDataFlavorSupported(BlockSelection.OBJECT_DATA_FLAVOR))
		{
			return false;
		}

		final BlockComponent component = (BlockComponent) support.getComponent();

		if (component.getDropLayer() == null)
		{
			return false;
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
	 */
	@Override
	public boolean importData(JComponent component, Transferable transferable)
	{
		if (!transferable.isDataFlavorSupported(BlockSelection.OBJECT_DATA_FLAVOR))
		{
			return false;
		}

		BlockComponent blockComponent = (BlockComponent) component;

		if (blockComponent.getDropLayer() == null)
		{
			return false;
		}

		try
		{
			DropLayer dropLayer = ((BlockComponent) component).getDropLayer();

			dropLayer.setBlockSelection((BlockSelection) transferable
					.getTransferData(BlockSelection.OBJECT_DATA_FLAVOR), true);
		}
		catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return true;
	}

}
