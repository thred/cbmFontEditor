package org.cbm.editor.font.ui.block;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;

import org.cbm.editor.font.model.Tool;

public interface Layer
{

    void setComponent(BlockComponent component);

    Cursor getCursor(Point mousePosition, Tool tool);

    void draw(Graphics2D g);

}
