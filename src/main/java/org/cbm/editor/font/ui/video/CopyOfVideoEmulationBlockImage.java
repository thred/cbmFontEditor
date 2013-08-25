package org.cbm.editor.font.ui.video;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.ui.block.image.AbstractBlockImage;
import org.cbm.editor.font.util.Palette;

public class CopyOfVideoEmulationBlockImage extends AbstractBlockImage
{

	private Carrier lumaCarrier;
	private Carrier chromaCarrier;
	private VideoEmulationImage image;

	public CopyOfVideoEmulationBlockImage(Font font, Block block)
	{
		super(font, block);
	}

	public CopyOfVideoEmulationBlockImage(Font font, Block block, Rectangle clip)
	{
		super(font, block, clip);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.block.image.AbstractBlockImage#render(org.cbm.editor.font.model.Font,
	 *      org.cbm.editor.font.model.Block, java.awt.Rectangle, double)
	 */
	@Override
	protected Image render(Font font, Block block, Rectangle clip, double zoom)
	{
		int posX = (clip != null) ? clip.x : 0;
		int posY = (clip != null) ? clip.y : 0;
		final int width = ((clip != null) ? clip.width : block.getWidth()) + 16;
		final int height = ((clip != null) ? clip.height : block.getHeight()) + 16;

		if ((width <= 0) || (height <= 0))
		{
			return null;
		}

		final VideoEmulationSettings settings = Registry.get(GlobalVideoEmulationSettings.class).get();
		final float lumaPerPixel = settings.getSignalMode().getStandard().getLumaPerPixel();
		final float chromaPerPixel = settings.getSignalMode().getStandard().getChromaPerPixel();
		final Carrier lumaCarrier = this.lumaCarrier = Carrier.create(this.lumaCarrier,
				(int) (lumaPerPixel * width) + 1);
		final Carrier chromaCarrier = this.chromaCarrier = Carrier.create(this.chromaCarrier,
				(int) (chromaPerPixel * width) + 1);
		final VideoEmulationImage image = VideoEmulationImage.create(this.image, settings, width, height);
		final boolean oddLinePhaseShift = settings.getSignalMode().getStandard().isOddLinePhaseShift();

		if (image == null)
		{
			return null;
		}

		for (int i = 0; i < 2; i += 1)
		{
//						final float shift = -settings.getShift() / 2 + (i * settings.getShift());

			for (int y = 0; y < height; y += 1)
			{
				final int sourceY = (y - 8) + clip.x;

				final float luma = block.getBackground().getL();

				lumaCarrier.setSourceOffset(0);

				for (int lCarrierX = 0; lCarrierX < lumaCarrier.getSourceLength(); lCarrierX += 1)
				{
					//					final float sourceX = lCarrierX / lPerPixel - 8 + view.y;
					//					final float target = getL(layer, sourceX, sourceY);
					//					final float diff = target - luma;
					//
					//					if (diff < 0)
					//					{
					//						luma += diff * (1f + settings.getLumaSharpness() * 0.7f);
					//					}
					//					else if (diff > 0)
					//					{
					//						luma += diff * (0.5f + settings.getLumaSharpness() * 0.4f);
					//					}
					//
					//					lCarrier.set(lCarrierX, luma);

					final float sourceX = ((lCarrierX / lumaPerPixel) - 8) + clip.y;
					final float target = getL(font, block, sourceX, sourceY);

					lumaCarrier.set(lCarrierX, target);

				}

				chromaCarrier.setSourceOffset(0);

				for (int chromaCarrierX = 0; chromaCarrierX < chromaCarrier.getSourceLength(); chromaCarrierX += 1)
				{
					final float sourceX = ((chromaCarrierX / chromaPerPixel) - 8) + clip.y;

					if ((chromaCarrierX % 2) == 0)
					{
						final float target = getI(font, block, sourceX, sourceY, (oddLinePhaseShift) && ((y % 2) == 1));

						chromaCarrier.set(chromaCarrierX, target);
					}
					else
					{
						final float target = getQ(font, block, sourceX, sourceY, false);

						chromaCarrier.set(chromaCarrierX, target);
					}
				}

				image.write(lumaCarrier, chromaCarrier, (y * 2) + i);
			}
		}

		this.image = image;

		// TODO Auto-generated method stub
		return null;
	}

	private float getL(Font font, final Block block, final float x, final int y)
	{
		final int lowerX = (int) Math.floor(x);
		final int upperX = (int) Math.ceil(x);

		final Palette lowerColor = getColor(font, block, lowerX, y);
		final Palette upperColor = getColor(font, block, upperX, y);

		return (lowerColor.getL() * ((1 - x) + lowerX)) + (upperColor.getL() * (x - lowerX));
	}

	private Palette getColor(Font font, Block block, int x, int y)
	{
		if (!block.isXYInBounds(x, y))
		{
			return block.getBackground();
		}

		return block.getColor(font, x, y);
	}

	private float getI(Font font, final Block block, final float x, final int y, final boolean invert)
	{
		final int lowerX = (int) Math.floor(x);
		final int upperX = (int) Math.ceil(x);

		final Palette lowerColor = getColor(font, block, lowerX, y);
		final Palette upperColor = getColor(font, block, upperX, y);

		return ((lowerColor.getU() * ((1 - x) + lowerX)) + (upperColor.getU() * (x - lowerX))) * ((invert) ? -1 : 1);
	}

	private float getQ(Font font, final Block block, final float x, final int y, final boolean invert)
	{
		final int lowerX = (int) Math.floor(x);
		final int upperX = (int) Math.ceil(x);

		final Palette lowerColor = getColor(font, block, lowerX, y);
		final Palette upperColor = getColor(font, block, upperX, y);

		return ((lowerColor.getV() * ((1 - x) + lowerX)) + (upperColor.getV() * (x - lowerX))) * ((invert) ? -1 : 1);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.cbm.editor.font.ui.block.image.AbstractBlockImage#paint(java.awt.Graphics2D, java.awt.Point, Rectangle,
	 *      double)
	 */
	@Override
	protected void paint(Graphics2D g, Point positionInPixel, Rectangle clip, double zoom)
	{
		if (image != null)
		{
			final int x = (int) ((clip.x - 8) * zoom);
			final int y = (int) ((clip.y - 8) * zoom);
			final int width = (int) (((clip.width - clip.x) + 16) * zoom);
			final int height = (int) (((clip.height - clip.y) + 16) * zoom);

									g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(), image.getHeight(), null);
		}
	}

}
