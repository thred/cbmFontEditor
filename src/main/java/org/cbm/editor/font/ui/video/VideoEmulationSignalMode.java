package org.cbm.editor.font.ui.video;

import java.util.ResourceBundle;

public enum VideoEmulationSignalMode
{

	MOS_6567_VIA_COMPOSITE(VideoEmulationStandard.NTSC_COMPOSITE),

	MOS_6567_VIA_S_VIDEO(VideoEmulationStandard.NTSC_S_VIDEO),

	MOS_6567_RF_MODULATED(VideoEmulationStandard.NTSC_RF_MODULATED),

	MOS_8562_VIA_COMPOSITE(VideoEmulationStandard.NTSC_COMPOSITE),

	MOS_8562_VIA_S_VIDEO(VideoEmulationStandard.NTSC_S_VIDEO),

	MOS_8562_RF_MODULATED(VideoEmulationStandard.NTSC_RF_MODULATED),

	MOS_6569_VIA_COMPOSITE(VideoEmulationStandard.PAL_BG_COMPOSITE),

	MOS_6569_VIA_S_VIDEO(VideoEmulationStandard.PAL_BG_S_VIDEO),

	MOS_6569_RF_MODULATED(VideoEmulationStandard.PAL_BG_RF_MODULATED),

	MOS_8565_VIA_COMPOSITE(VideoEmulationStandard.PAL_BG_COMPOSITE),

	MOS_8565_VIA_S_VIDEO(VideoEmulationStandard.PAL_BG_S_VIDEO),

	MOS_8565_RF_MODULATED(VideoEmulationStandard.PAL_BG_RF_MODULATED);

	private final String name;
	private final String description;
	private final VideoEmulationStandard standard;

	private VideoEmulationSignalMode(final VideoEmulationStandard standard)
	{
		this.standard = standard;

		final ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName().replace('.', '/'));

		name = bundle.getString(name() + ".name");
		description = bundle.getString(name() + ".description");
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public VideoEmulationStandard getStandard()
	{
		return standard;
	}

	/**
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}

}
