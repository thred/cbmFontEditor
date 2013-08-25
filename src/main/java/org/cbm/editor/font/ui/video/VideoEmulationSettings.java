package org.cbm.editor.font.ui.video;

public class VideoEmulationSettings
{

	private VideoEmulationSignalMode signalMode = VideoEmulationSignalMode.MOS_6569_RF_MODULATED;
	private VideoEmulationDisplayMode displayMode = VideoEmulationDisplayMode.TV_SET;

	private float lumaSharpness = 0;
	private float shift = 0;
	private float physicalResolution = 1;

	public VideoEmulationSettings()
	{
		super();
	}

	public VideoEmulationSettings(final VideoEmulationSignalMode signalMode, final VideoEmulationDisplayMode displayMode, final float lumaSharpness, final float shift, final float physicalResolution)
	{
		super();

		setSignalMode(signalMode);
		setDisplayMode(displayMode);
		setLumaSharpness(lumaSharpness);
		setShift(shift);
		setPhysicalResolution(physicalResolution);
	}

	public VideoEmulationSettings(final VideoEmulationSettings settings)
	{
		super();

		set(settings);
	}

	public VideoEmulationSettings get()
	{
		return new VideoEmulationSettings(this);
	}

	public void set(final VideoEmulationSettings settings)
	{
		setSignalMode(settings.getSignalMode());
		setDisplayMode(settings.getDisplayMode());
		setLumaSharpness(settings.getLumaSharpness());
		setShift(settings.getShift());
		setPhysicalResolution(settings.getPhysicalResolution());
	}

	public VideoEmulationSignalMode getSignalMode()
	{
		return signalMode;
	}

	public boolean setSignalMode(final VideoEmulationSignalMode signalMode)
	{
		if (this.signalMode != signalMode)
		{
			this.signalMode = signalMode;

			return true;
		}

		return false;
	}

	public VideoEmulationDisplayMode getDisplayMode()
	{
		return displayMode;
	}

	public boolean setDisplayMode(final VideoEmulationDisplayMode displayMode)
	{
		if (this.displayMode != displayMode)
		{
			this.displayMode = displayMode;

			return true;
		}

		return false;
	}

	public float getLumaSharpness()
	{
		return lumaSharpness;
	}

	public boolean setLumaSharpness(final float lumaSharpness)
	{
		if (this.lumaSharpness != lumaSharpness)
		{
			this.lumaSharpness = lumaSharpness;

			return true;
		}

		return false;
	}

	public float getShift()
	{
		return shift;
	}

	public boolean setShift(final float shift)
	{
		if (this.shift != shift)
		{
			this.shift = shift;

			return true;
		}

		return false;
	}

	public float getPhysicalResolution()
	{
		return physicalResolution;
	}

	public boolean setPhysicalResolution(final float physicalResolution)
	{
		if (this.physicalResolution != physicalResolution)
		{
			this.physicalResolution = physicalResolution;

			return true;
		}

		return false;
	}

	public boolean isImageCompatible(final VideoEmulationSettings settings)
	{
		if (settings == null)
		{
			return false;
		}

		return ((signalMode.equals(settings.getSignalMode())) && (displayMode.equals(settings.getDisplayMode())) && (physicalResolution == settings.getPhysicalResolution()));
	}

}
