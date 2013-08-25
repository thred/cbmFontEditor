package org.cbm.editor.font.ui.video;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class VideoEmulationImage extends BufferedImage
{

	private final VideoEmulationSettings settings;

	private VideoEmulationImage(final VideoEmulationSettings settings, final int imageWidth, final int imageHeight)
	{
		super(imageWidth, imageHeight, TYPE_INT_RGB);

		this.settings = settings;
	}

	public VideoEmulationSettings getSettings()
	{
		return settings;
	}

	public void write(final Carrier lumaCarrier, final Carrier chromaCarrier, final int y)
	{
		final int imageWidth = getWidth();
		final int[] rgb = new int[3];
		final WritableRaster raster = getRaster();

		lumaCarrier.setTargetLength(imageWidth);
		chromaCarrier.setTargetLength(imageWidth);

		switch (settings.getDisplayMode())
		{
			case LUMINANCE_CHANNEL:
				for (int x = 0; x < imageWidth; x += 1)
				{
					y2rgb(rgb, lumaCarrier.get(x));

					raster.setPixel(x, y, rgb);
				}
				break;

			case CHROMINANCE_CHANNEL:
				for (int x = 0; x < imageWidth; x += 1)
				{
					yuv2rgb(rgb, 128, chromaCarrier.get(x, 0 + settings.getShift()),
					    chromaCarrier.get(x, 1 + settings.getShift()));

					raster.setPixel(x, y, rgb);
				}
				break;

			case CHROMINANCE_I_CHANNEL:
				for (int x = 0; x < imageWidth; x += 1)
				{
					yuv2rgb(rgb, 128, chromaCarrier.get(x, 0 + settings.getShift()), 0);

					raster.setPixel(x, y, rgb);
				}
				break;

			case CHROMINANCE_Q_CHANNEL:
				for (int x = 0; x < imageWidth; x += 1)
				{
					yuv2rgb(rgb, 128, 0, chromaCarrier.get(x, 1 + settings.getShift()));

					raster.setPixel(x, y, rgb);
				}
				break;

			case BLACK_AND_WHITE_TV:
				for (int x = 0; x < imageWidth; x += 1)
				{
					final float luma = lumaCarrier.get(x);
					final float chroma = chromaCarrier.get(x);

					y2rgb(rgb, luma + 0.2f * chroma);

					raster.setPixel(x, y, rgb);
				}
				break;

			case MONITOR:
			case TV_SET:
				for (int x = 0; x < imageWidth; x += 1)
				{
					final float luma = lumaCarrier.get(x);

					float i = chromaCarrier.get(x, 0 + settings.getShift());
					float q = chromaCarrier.get(x, 1 + settings.getShift());

					if ((settings.getSignalMode().getStandard().isOddLinePhaseShift()) && ((y / 2) % 2 == 1))
					{
						i = -i;
					}

					yuv2rgb(rgb, luma, i, q);

					raster.setPixel(x, y, rgb);
				}
				break;
		}

	}

	private static int[] y2rgb(final int[] rgb, final float l)
	{
		rgb[0] = range((int) l, 0, 255);
		rgb[1] = range((int) l, 0, 255);
		rgb[2] = range((int) l, 0, 255);

		return rgb;
	}

	private static int[] yuv2rgb(final int[] rgb, final float l, final float u, final float v)
	{
		rgb[0] = range((int) (l + 1.140f * v), 0, 255);
		rgb[1] = range((int) (l - 0.396f * u - 0.581f * v), 0, 255);
		rgb[2] = range((int) (l + 2.029f * u), 0, 255);

		return rgb;
	}

	private static int range(final int value, final int min, final int max)
	{
		if (value > max)
		{
			return max;
		}

		if (value < min)
		{
			return min;
		}

		return value;
	}

	private static float range(final float value, final float min, final float max)
	{
		if (value > max)
		{
			return max;
		}

		if (value < min)
		{
			return min;
		}

		return value;
	}

	public static VideoEmulationImage create(final VideoEmulationImage existing, final VideoEmulationSettings settings, final int width, final int height)
	{
		int imageWidth = 0;
		final int imageHeight = height * 2;

		switch (settings.getDisplayMode())
		{
			case LUMINANCE_CHANNEL:
				imageWidth = (int) (settings.getSignalMode().getStandard().getLumaPerPixel() * width) + 1;
				break;

			case CHROMINANCE_CHANNEL:
				imageWidth = (int) (settings.getSignalMode().getStandard().getLumaPerPixel() * width) + 1;
				break;

			case CHROMINANCE_I_CHANNEL:
				imageWidth = (int) (settings.getSignalMode().getStandard().getChromaPerPixel() * width) + 1;
				break;

			case CHROMINANCE_Q_CHANNEL:
				imageWidth = (int) (settings.getSignalMode().getStandard().getChromaPerPixel() * width) + 1;
				break;

			case BLACK_AND_WHITE_TV:
				imageWidth =
				    (int) (settings.getSignalMode().getStandard().getLumaPerPixel() * width * settings.getPhysicalResolution()) + 1;
				break;

			case MONITOR:
				imageWidth = (int) (settings.getSignalMode().getStandard().getLumaPerPixel() * width * 2) + 1;
				break;

			case TV_SET:
				imageWidth = (int) (settings.getSignalMode().getStandard().getLumaPerPixel() * width) + 1;
				break;

			default:
				imageWidth = (int) (settings.getSignalMode().getStandard().getLumaPerPixel() * width) + 1;
				break;
		}

		if ((existing == null) || (imageWidth != existing.getWidth()) || (imageHeight != existing.getHeight())
		    || (!settings.isImageCompatible(existing.getSettings())))
		{
			if ((imageWidth > 0) && (imageHeight > 0))
			{
				return new VideoEmulationImage(settings.get(), imageWidth, imageHeight);
			}

			return null;
		}

		existing.getSettings().set(settings);

		return existing;
	}

}
