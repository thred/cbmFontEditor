package org.cbm.editor.font.ui.video;

import org.cbm.editor.font.util.Events;
import org.cbm.editor.font.util.Prefs;

public class GlobalVideoEmulationSettings extends VideoEmulationSettings
{

	public GlobalVideoEmulationSettings()
	{
		super();

		load();
	}

	/**
	 * @see org.cbm.editor.font.ui.video.VideoEmulationSettings#setSignalMode(org.cbm.editor.font.ui.video.VideoEmulationSignalMode)
	 */
	@Override
	public boolean setSignalMode(final VideoEmulationSignalMode signalMode)
	{
		if (super.setSignalMode(signalMode))
		{
			Events.fire(this, new VideoEmulationSettingsModifiedEvent());

			return true;
		}

		return false;
	}

	/**
	 * @see org.cbm.editor.font.ui.video.VideoEmulationSettings#setDisplayMode(org.cbm.editor.font.ui.video.VideoEmulationDisplayMode)
	 */
	@Override
	public boolean setDisplayMode(final VideoEmulationDisplayMode displayMode)
	{
		if (super.setDisplayMode(displayMode))
		{
			Events.fire(this, new VideoEmulationSettingsModifiedEvent());

			return true;
		}

		return false;
	}

	@Override
	public boolean setLumaSharpness(final float lumaSharpness)
	{
		if (super.setLumaSharpness(lumaSharpness))
		{
			Events.fire(this, new VideoEmulationSettingsModifiedEvent());

			return true;
		}

		return false;
	}

	@Override
	public boolean setShift(final float shift)
	{
		if (super.setShift(shift))
		{
			Events.fire(this, new VideoEmulationSettingsModifiedEvent());

			return true;
		}

		return false;
	}

	@Override
	public boolean setPhysicalResolution(final float physicalResolution)
	{
		if (super.setPhysicalResolution(physicalResolution))
		{
			Events.fire(this, new VideoEmulationSettingsModifiedEvent());

			return true;
		}

		return false;
	}

	public void load()
	{
		Events.disable();

		try
		{
			setSignalMode(VideoEmulationSignalMode.valueOf(Prefs.get("signalMode", getSignalMode().name())));
			setDisplayMode(VideoEmulationDisplayMode.valueOf(Prefs.get("displayMode", getDisplayMode().name())));
			setLumaSharpness(Prefs.get("lumaSharpness", getLumaSharpness()));
			setShift(Prefs.get("shift", getShift()));
			setPhysicalResolution(Prefs.get("physicalResolution", getPhysicalResolution()));
		}
		catch (final IllegalArgumentException e)
		{
			e.printStackTrace(System.err);
		}
		finally
		{
			Events.enable();
		}

		Events.fire(this, new VideoEmulationSettingsModifiedEvent());
	}

	public void store()
	{
		Prefs.set("signalMode", getSignalMode().name());
		Prefs.set("displayMode", getDisplayMode().name());
		Prefs.set("lumaSharpness", getLumaSharpness());
		Prefs.set("shift", getShift());
		Prefs.set("physicalResolution", getPhysicalResolution());
	}

	public void reset()
	{

	}
}
