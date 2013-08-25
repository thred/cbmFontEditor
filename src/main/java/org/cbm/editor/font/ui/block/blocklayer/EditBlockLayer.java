package org.cbm.editor.font.ui.block.blocklayer;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.cbm.editor.font.CBMCursor;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.BlockAdapter;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.Tool;
import org.cbm.editor.font.model.events.BlockModifiedEvent;
import org.cbm.editor.font.model.events.BlockSwitchedEvent;
import org.cbm.editor.font.model.events.FontModifiedEvent;
import org.cbm.editor.font.model.events.FontSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;
import org.cbm.editor.font.ui.action.FillEdit;
import org.cbm.editor.font.ui.block.image.BlockImage;
import org.cbm.editor.font.ui.block.image.DefaultBlockImage;
import org.cbm.editor.font.ui.block.select.Selection;
import org.cbm.editor.font.ui.block.select.SelectionLayer;
import org.cbm.editor.font.ui.edit.Draw;
import org.cbm.editor.font.ui.video.VideoEmulationBlockImage;

public class EditBlockLayer extends AbstractBlockLayer implements MouseListener, MouseMotionListener
{

	private final FontAdapter fontAdapter;
	private final BlockAdapter blockAdapter;

	private BlockImage image;
	protected Draw draw = null;

	public EditBlockLayer()
	{
		super();

		Registry.get(ProjectAdapter.class).bind(this);
		
		fontAdapter = Registry.get(FontAdapter.class).bind(this);
		blockAdapter = Registry.get(BlockAdapter.class).bind(this);

		updateState();
	}

	public void handleEvent(ProjectSwitchedEvent event)
	{
		updateState();
	}

	public void handleEvent(FontSwitchedEvent event)
	{
		updateState();
	}

	public void handleEvent(BlockSwitchedEvent event)
	{
		updateState();
	}

	public void handleEvent(FontModifiedEvent event)
	{
		repaint();
	}

	public void handleEvent(BlockModifiedEvent event)
	{
		repaint();
	}

	private void updateState()
	{
		invalidateImage();
		revalidate();
	}

	@Override
	public void invalidateImage()
	{
		if (image != null)
		{
			image.destroy();
		}

		image = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getFont()
	 */
	@Override
	public Font getFont()
	{
		return fontAdapter.getFont();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#getBlock()
	 */
	@Override
	public Block getBlock()
	{
		return blockAdapter.getBlock();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.block.blocklayer.AbstractBlockLayer#getCursor(java.awt.Point,
	 *      org.cbm.editor.font.model.Tool)
	 */
	@Override
	public Cursor getCursor(Point positionInComponent, Tool tool)
	{
		Block block = blockAdapter.getBlock();

		if (block == null)
		{
			return CBMCursor.POINTER.getCursor();
		}

		Point point = null;

		switch (tool)
		{
			case POINTER:
				return CBMCursor.POINTER.getCursor();

			case SELECTION:
				return CBMCursor.SELECTION.getCursor();

			case DRAW:
				point = getComponent().convertFromComponentToCharacter(positionInComponent);

				if ((block.isCharacterXYInBounds(point.x, point.y)) && (block.getCharacter(point.x, point.y) >= 0))
				{
					return CBMCursor.DRAW.getCursor();
				}
				break;

			case FILL:
				point = getComponent().convertFromComponentToCharacter(positionInComponent);

				if ((block.isCharacterXYInBounds(point.x, point.y)) && (block.getCharacter(point.x, point.y) >= 0))
				{
					return CBMCursor.FILL.getCursor();
				}
				break;
		}

		return CBMCursor.POINTER.getCursor();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.block.blocklayer.BlockLayer#draw(java.awt.Graphics2D, double)
	 */
	@Override
	public void draw(Graphics2D g)
	{
		double zoom = getComponent().getZoom();
		Font font = getFont();

		if (font == null)
		{
			return;
		}

		Block block = blockAdapter.getBlock();

		if (block == null)
		{
			return;
		}

		BlockImage currentImage = image;

		if (currentImage == null)
		{
			currentImage = (getComponent().isPal()) ? new VideoEmulationBlockImage(font, block)
					: new DefaultBlockImage(font, block);
			image = currentImage;
		}

		currentImage.draw(g, new Point(getXInPixel(), getYInPixel()), zoom);

		drawGrid(g);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		// intentionally left blank
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		if (getBlock() == null)
		{
			return;
		}

		if (e.isConsumed())
		{
			return;
		}

		final Point positionInComponent = getComponent().transfer(e.getPoint());
		final int modifiers = e.getModifiers();

		if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
		{
			if (getTool() == Tool.DRAW)
			{
				draw = new Draw(fontAdapter.getFont(), getBlock(), getComponent().convertFromComponentToPixel(
						positionInComponent));
				e.consume();
				return;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (getBlock() == null)
		{
			return;
		}

		if (e.isConsumed())
		{
			return;
		}

		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
		{
			final Point position = getComponent().convertFromComponentToPixel(getComponent().transfer(e.getPoint()));

			if (draw != null)
			{
				draw.finish(position);
				draw = null;
				e.consume();
				return;
			}
			else if (getTool() == Tool.FILL)
			{
				final Block block = getBlock();

				if (!block.isXYInBounds(position.x, position.y))
				{
					return;
				}

				SelectionLayer selectionLayer = getComponent().getSelectionLayer();
				Selection selection = selectionLayer.getSelection();

				if ((selection != null) && (!selection.contains(position)))
				{
					return;
				}

				if (block.getCharacter(position.x / block.getCharacterWidth(), position.y / block.getCharacterHeight()) < 0)
				{
					return;
				}

				Registry.execute(new FillEdit(fontAdapter.getFont(), block, position, (selection != null) ? selection
						.getRectangle() : null));
				e.consume();
				return;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		// intentionally left blank
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		// intentionally left blank
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (e.isConsumed())
		{
			return;
		}

		if (e.isConsumed())
		{
			return;
		}

		if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK)
		{
			if (draw != null)
			{
				draw.next(getComponent().convertFromComponentToPixel(getComponent().transfer(e.getPoint())));
				e.consume();
				return;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		// intentionally left blank
	}

}
