package org.cbm.editor.font.ui.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;

public class Colors
{

    public static Color darker(Color color, float factor)
    {
        return new Color(Math.max((int) (color.getRed() * factor), 0), Math.max((int) (color.getGreen() * factor), 0),
            Math.max((int) (color.getBlue() * factor), 0), color.getAlpha());
    }

    public static Color brighter(Color color, float factor)
    {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0)
        {
            return new Color(i, i, i, alpha);
        }
        if (r > 0 && r < i)
        {
            r = i;
        }
        if (g > 0 && g < i)
        {
            g = i;
        }
        if (b > 0 && b < i)
        {
            b = i;
        }

        return new Color(Math.min((int) (r / factor), 255), Math.min((int) (g / factor), 255),
            Math.min((int) (b / factor), 255), alpha);
    }

    public static void backgroundLowered(Graphics graphics, Color color, int width, int height)
    {
        backgroundLowered(graphics, color, color, width, height);
    }

    public static void backgroundLowered(Graphics graphics, Color highlight, Color color, int width, int height)
    {
        final Paint paint = new LinearGradientPaint(0, 0, 0, height, new float[]{0f, 0.03f, 0.97f, 1f},
            new Color[]{Colors.darker(highlight, 0.1f), Colors.darker(highlight, 0.9f), color, color});

        final Graphics2D g = (Graphics2D) graphics;

        g.setPaint(paint);
        g.fillRect(0, 0, width, height);
    }

    public static void backgroundBottom(Graphics graphics, Color color, int width, int height)
    {
        final Paint paint = new LinearGradientPaint(0, 0, 0, height, new float[]{0f, 0.03f, 0.95f, 1f},
            new Color[]{Colors.darker(color, 0.7f), color, Colors.darker(color, 0.9f), Colors.darker(color, 0.1f)});

        final Graphics2D g = (Graphics2D) graphics;

        g.setPaint(paint);
        g.fillRect(0, 0, width, height);
    }

}
