package org.cbm.editor.font.ui.video;

public class Carrier
{

	private final int sourceLength;
	private final float[] source;

	private float sourceOffset;
	private int targetLength;

	public Carrier(final int sourceLength)
	{
		super();

		this.sourceLength = sourceLength;

		source = new float[sourceLength];
	}

	public float getSourceOffset()
	{
		return sourceOffset;
	}

	public void setSourceOffset(final float sourceOffset)
	{
		this.sourceOffset = sourceOffset;
	}

	public int getTargetLength()
	{
		return targetLength;
	}

	public void setTargetLength(final int targetLength)
	{
		this.targetLength = targetLength;
	}

	public int getSourceLength()
	{
		return sourceLength;
	}

	public void set(final int sourceIndex, final float value)
	{
		source[sourceIndex] = value;
	}

	private float getSource(final float sourceIndex)
    {
	    final int lowerSourceIndex = (int) Math.floor(sourceIndex);
		final int upperSourceIndex = (int) Math.ceil(sourceIndex);

		if (lowerSourceIndex < 0)
		{
			return source[0];
		}

		if (upperSourceIndex > sourceLength - 1)
		{
			return source[sourceLength - 1];
		}

		final float lowerSource = source[lowerSourceIndex];
		final float upperSource = source[upperSourceIndex];

		return lowerSource;
		
//		return lowerSource * (1 - sourceIndex + lowerSourceIndex) + upperSource * (sourceIndex - lowerSourceIndex);
    }

	public float get(final int targetIndex)
	{
		final float sourceIndex = sourceOffset + (float)targetIndex * sourceLength / targetLength;

		return getSource(sourceIndex);
	}

	public float get(final int targetIndex, float phase)
	{
		final float sourceIndex = sourceOffset + (float)targetIndex * sourceLength / 2 / targetLength;

		int lowerSourceIndex = (int) Math.floor(sourceIndex);
		int upperSourceIndex = (int) Math.ceil(sourceIndex);
		
		
		final float lowerSource = getSource((lowerSourceIndex * 2 + phase)); 
		final float upperSource = getSource((upperSourceIndex * 2 + phase));

		return lowerSource * (1 - sourceIndex + lowerSourceIndex) + upperSource * (sourceIndex - lowerSourceIndex);
	}

	public static Carrier create(final Carrier existing, final int sourceLength)
	{
		if ((existing != null) && (existing.getSourceLength() == sourceLength))
		{
			return existing;
		}

		return new Carrier(sourceLength);
	}

}
