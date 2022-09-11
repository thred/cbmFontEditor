package org.cbm.editor.font.ui.video;

import java.util.ResourceBundle;

public enum VideoEmulationStandard
{

    NTSC_COMPOSITE(60f, 2, false, 402, 292),

    NTSC_S_VIDEO(60f, 2, false, 402, 292),

    NTSC_RF_MODULATED(60f, 15734f, 4200000f, 1700000f, 2, 0.8125f, 0.92f, false, 402, 292),

    PAL_BG_COMPOSITE(50f, 4, true, 402, 292),

    PAL_BG_S_VIDEO(50f, 4, true, 402, 292),

    PAL_BG_RF_MODULATED(50f, 15625f, 5000000f, 1950000f, 4, 0.8125f, 0.92f, true, 402, 292);

    private final String name;
    private final float frameFrequency;
    private final float lineFrequency;
    private final float videoBandwidth;
    private final float colorBandwidth;
    private final int colorShiftLines;
    private final float horizontalVisible;
    private final float verticalVisible;
    private final boolean oddLinePhaseShift;
    private final int horizontalResolution;
    private final int verticalResolution;

    private final float lumaResolution;
    private final float lumaPerPixel;
    private final float chromaResolution;
    private final float chromaPerPixel;

    private VideoEmulationStandard(final float frameFrequency, final int colorShiftLines,
        final boolean oddLinePhaseShift, final int horizontalResolution, final int verticalResolution)
    {
        this.frameFrequency = frameFrequency;
        lineFrequency = -1;
        videoBandwidth = -1;
        colorBandwidth = -1;
        horizontalVisible = -1;
        verticalVisible = -1;
        this.colorShiftLines = colorShiftLines;
        this.oddLinePhaseShift = oddLinePhaseShift;
        this.horizontalResolution = horizontalResolution;
        this.verticalResolution = verticalResolution;

        lumaResolution = horizontalResolution;
        lumaPerPixel = 1;

        chromaResolution = horizontalResolution;
        chromaPerPixel = 1;

        final ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName().replace('.', '/'));

        name = bundle.getString(name() + ".name");
    }

    private VideoEmulationStandard(final float frameFrequency, final float lineFrequency, final float videoBandwidth,
        final float colorBandwidth, final int colorShiftLines, final float horizontalVisible,
        final float verticalVisible, final boolean oddLinePhaseShift, final int horizontalResolution,
        final int verticalResolution)
    {
        this.frameFrequency = frameFrequency;
        this.lineFrequency = lineFrequency;
        this.videoBandwidth = videoBandwidth;
        this.colorBandwidth = colorBandwidth;
        this.colorShiftLines = colorShiftLines;
        this.horizontalVisible = horizontalVisible;
        this.verticalVisible = verticalVisible;
        this.horizontalResolution = horizontalResolution;
        this.verticalResolution = verticalResolution;
        this.oddLinePhaseShift = oddLinePhaseShift;

        lumaResolution = videoBandwidth / lineFrequency * 2 * horizontalVisible;
        lumaPerPixel = lumaResolution / horizontalResolution;

        chromaResolution = colorBandwidth / lineFrequency * 2 * horizontalVisible;
        chromaPerPixel = chromaResolution / horizontalResolution;

        final ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName().replace('.', '/'));

        name = bundle.getString(name() + ".name");
    }

    public String getName()
    {
        return name;
    }

    public float getFrameFrequency()
    {
        return frameFrequency;
    }

    public float getLineFrequency()
    {
        return lineFrequency;
    }

    public float getVideoBandwidth()
    {
        return videoBandwidth;
    }

    public float getColorBandwidth()
    {
        return colorBandwidth;
    }

    public int getColorShiftLines()
    {
        return colorShiftLines;
    }

    public float getHorizontalVisible()
    {
        return horizontalVisible;
    }

    public float getVerticalVisible()
    {
        return verticalVisible;
    }

    public boolean isOddLinePhaseShift()
    {
        return oddLinePhaseShift;
    }

    public int getHorizontalResolution()
    {
        return horizontalResolution;
    }

    public int getVerticalResolution()
    {
        return verticalResolution;
    }

    public float getLumaResolution()
    {
        return lumaResolution;
    }

    public float getLumaPerPixel()
    {
        return lumaPerPixel;
    }

    public float getChromaResolution()
    {
        return chromaResolution;
    }

    public float getChromaPerPixel()
    {
        return chromaPerPixel;
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
