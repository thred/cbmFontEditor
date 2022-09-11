package org.cbm.editor.font.util;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.Icon;

/**
 * The VIC II Color Palette<br/>
 * <br/>
 * The true colors are based on the analysis of Philip "Pepto" Timmermann<br/>
 * <a href="http://unusedino.de/ec64/technical/misc/vic656x/colors/index.html"
 * >http://unusedino.de/ec64/technical/misc/vic656x/colors/index.html</a>
 *
 * @author Manfred HANTSCHEL
 */
public enum Palette
{

    BLACK("Black", KeyEvent.VK_0, 0x00, false, 0.0f, 0.0f, 0.0f),
    WHITE("White", KeyEvent.VK_1, 0x01, false, 255.0f, 0.0f, 0.0f),
    RED("Red", KeyEvent.VK_2, 0x02, false, 79.6875f, -13.01434923670814368f, +31.41941843272073796f),
    CYAN("Cyan", KeyEvent.VK_3, 0x03, false, 159.375f, +13.01434923670814368f, -31.41941843272073796f),
    PURPLE("Purple", KeyEvent.VK_4, 0x04, false, 95.625f, +24.04738177749708281f, +24.04738177749708281f),
    GREEN("Green", KeyEvent.VK_5, 0x05, false, 127.5f, -24.04738177749708281f, -24.04738177749708281f),
    BLUE("Blue", KeyEvent.VK_6, 0x06, false, 63.75f, +34.081334493f, 0f),
    YELLOW("Yellow", KeyEvent.VK_7, 0x07, false, 191.25f, -34.0081334493f, 0f),

    ORANGE("Orange", KeyEvent.VK_8, 0x08, true, 95.625f, -24.04738177749708281f, +24.04738177749708281f),
    BROWN("Brown", KeyEvent.VK_9, 0x09, true, 63.75f, -31.41941843272073796f, +13.01434923670814368f),
    LIGHT_RED("Light Red", KeyEvent.VK_A, 0x0a, true, 127.5f, -13.01434923670814368f, +31.41941843272073796f),
    DARK_GRAY("Dark Gray", KeyEvent.VK_B, 0x0b, true, 79.6875f, 0f, 0f),
    GRAY("Gray", KeyEvent.VK_C, 0x0c, true, 119.53125f, 0f, 0f),
    LIGHT_GREEN("Light Green", KeyEvent.VK_D, 0x0d, true, 191.25f, -24.04738177749708281f, -24.04738177749708281f),
    LIGHT_BLUE("Light Blue", KeyEvent.VK_E, 0x0e, true, 119.53125f, +34.0081334493f, 0f),
    LIGHT_GRAY("Light Gray", KeyEvent.VK_F, 0x0f, true, 159.375f, 0f, 0f);

    private final String name;
    private final int mnemonic;
    private final int index;
    private final boolean triggersMultiColor;
    private final float l;
    private final float u;
    private final float v;
    private final int[] rgb;
    private final int[] argb;
    private final Color color;

    private final Icon icon;

    Palette(final String name, final int mnemonic, final int index, final boolean triggersMultiColor, final float l,
        final float u, final float v)
    {
        this.name = name;
        this.mnemonic = mnemonic;
        this.index = index;
        this.triggersMultiColor = triggersMultiColor;
        this.l = l;
        this.u = u;
        this.v = v;

        rgb = yuv2rgb(new int[3], l, u, v);
        argb = yuv2argb(new int[4], l, u, v);
        color = new Color(rgb[0], rgb[1], rgb[2]);
        icon = new PaletteIcon(color);
    }

    public String getName()
    {
        return name + " (0x0" + Integer.toHexString(index) + ")";
    }

    public int getMnemonic()
    {
        return mnemonic;
    }

    public int getIndex()
    {
        return index;
    }

    public boolean isTriggersMultiColor()
    {
        return triggersMultiColor;
    }

    public float getL()
    {
        return l;
    }

    public float getU()
    {
        return u;
    }

    public float getV()
    {
        return v;
    }

    public int[] getRGB()
    {
        return rgb;
    }

    public int[] getARGB()
    {
        return argb;
    }

    public Color getColor()
    {
        return color;
    }

    public Icon getIcon()
    {
        return icon;
    }

    public static int[] y2rgb(final int[] rgb, final float l)
    {
        rgb[0] = range((int) l, 0, 255);
        rgb[1] = range((int) l, 0, 255);
        rgb[2] = range((int) l, 0, 255);

        return rgb;
    }

    public static int[] yuv2rgb(final int[] rgb, final float l, final float u, final float v)
    {
        rgb[0] = range((int) (l + 1.140f * v), 0, 255);
        rgb[1] = range((int) (l - 0.396f * u - 0.581f * v), 0, 255);
        rgb[2] = range((int) (l + 2.029f * u), 0, 255);

        return rgb;
    }

    public static int[] yuv2argb(final int[] rgb, final float l, final float u, final float v)
    {
        rgb[0] = range((int) (l + 1.140f * v), 0, 255);
        rgb[1] = range((int) (l - 0.396f * u - 0.581f * v), 0, 255);
        rgb[2] = range((int) (l + 2.029f * u), 0, 255);
        rgb[3] = 0xff;

        return rgb;
    }

    public static int range(final int value, final int min, final int max)
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

    public static float range(final float value, final float min, final float max)
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

}
