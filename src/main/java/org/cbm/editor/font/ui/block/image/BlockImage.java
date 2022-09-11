package org.cbm.editor.font.ui.block.image;

import java.awt.Graphics2D;
import java.awt.Point;

public interface BlockImage
{

    void draw(Graphics2D g, Point positionInPixel, double zoom);

    void destroy();

}
