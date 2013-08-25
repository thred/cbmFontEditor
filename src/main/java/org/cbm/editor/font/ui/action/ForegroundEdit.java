package org.cbm.editor.font.ui.action;

import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Palette;

public class ForegroundEdit extends AbstractEdit
{

	private static final long serialVersionUID = 7708688005476487002L;

	private final Block block;
	private final Collection<Point> positionsInCharacter;
	private final Palette color;
	private final Map<Point, Palette> previousColors;

	public ForegroundEdit(Block block, Collection<Point> positionsInCharacter, Palette color)
	{
		super("Foreground");

		this.block = block;
		this.positionsInCharacter = Collections.unmodifiableCollection(positionsInCharacter);
		this.color = color;

		previousColors = new HashMap<Point, Palette>();
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
	 */
	@Override
	public void execute()
	{
		for (Point position : positionsInCharacter)
		{
			if (block.isCharacterXYInBounds(position.x, position.y))
			{
				previousColors.put(position, block.getForeground(position.x, position.y));
				block.setForeground(position.x, position.y, color);
			}
		}

		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Foreground of characters set to: " + color);
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
	 */
	@Override
	public void rollback()
	{
		for (Map.Entry<Point, Palette> entries : previousColors.entrySet())
		{
			block.setForeground(entries.getKey().x, entries.getKey().y, entries.getValue());
		}

		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Reverted foreground colors.");
	}

}
