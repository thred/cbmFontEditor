package org.cbm.editor.font;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum Icon
{

	ACCEPT_SELECTION("acceptSelection.png"),
	ADD("add.png"),
	ADD_MORE("addMore.png"),
	CLEAR_SELECTION("clearSelection.png"),
	CLOSE("close.png"),
	COLOR("color.png"),
	COMMODORE("commodore.png"),
	COPY("copy.png"),
	CURSOR_TOOL("cursorTool.png"),
	CUT("cut.png"),
	DELETE("delete.png"),
	DISMISS("dismiss.png"),
	DISMISS_SELECTION("dismissSelection.png"),
	DOWN("down.png"),
	DRAW_TOOL("drawTool.png"),
	EXPORT("export.png"),
	FILL_TOOL("fillTool.png"),
	GRID0("grid0.png"),
	GRID1("grid1.png"),
	GRID2("grid2.png"),
	GRID3("grid3.png"),
	GRID4("grid4.png"),
	GRID5("grid5.png"),
	GRID6("grid6.png"),
	GRID7("grid7.png"),
	GRID8("grid8.png"),
	GRID9("grid9.png"),
	GRID10("grid10.png"),
	GRID11("grid11.png"),
	GRID12("grid12.png"),
	GRID13("grid13.png"),
	GRID14("grid14.png"),
	GRID15("grid15.png"),
	HELP("help.png"),
	IMPORT("import.png"),
	LEFT("left.png"),
	NEW("new.png"),
	OPEN("open.png"),
	PAL("pal.png"),
	PASTE("paste.png"),
	POINTER_TOOL("pointerTool.png"),
	REDO("redo.png"),
	REMOVE("remove.png"),
	RIGHT("right.png"),
	SAVE("save.png"),
	SAVEAS("saveas.png"),
	SELECTION_TOOL("selectionTool.png"),
	SELECTOR("selector.png"),
	UNDO("undo.png"),
	UP("up.png"),
	ZOOM("zoom.png");

	private static final String PREFIX = "org/cbm/editor/";

	private final String resource;

	private Image image;

	private Icon(final String resource)
	{
		this.resource = PREFIX + resource;
	}

	public javax.swing.Icon getIcon()
	{
		return new ImageIcon(getImage());
	}

	public Image getImage()
	{
		if (image == null)
		{
			try
			{
				image = ImageIO.read(getClass().getClassLoader().getResource(resource));
			}
			catch (final IOException e)
			{
				throw new RuntimeException("Failed to load image: " + resource, e);
			}
		}

		return image;
	}

}
