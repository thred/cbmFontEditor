package org.cbm.editor.font.ui.action;

import java.awt.Rectangle;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Events;

public class EditAcceptEdit extends AbstractEdit
{

	private static final long serialVersionUID = -2012377693460397308L;

	private final Font targetFont;
	private final Block targetBlock;
	private final int xInPixel;
	private final int yInPixel;
	private final BlockSelection blockSelection;

	private Font backup;

	public EditAcceptEdit(Font targetFont, Block targetBlock, int xInPixel, int yInPixel, BlockSelection blockSelection)
	{
		super("Accept");

		this.targetFont = targetFont;
		this.targetBlock = targetBlock;
		this.xInPixel = xInPixel;
		this.yInPixel = yInPixel;
		this.blockSelection = blockSelection;
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
	 */
	@Override
	public void execute()
	{
		backup = targetFont.copy();
		Font sourceFont = blockSelection.getFont();
		Block sourceBlock = blockSelection.getBlock();
		Rectangle sourceRectangle = blockSelection.getRectangle();

		Events.disable();

		try
		{
			for (int y = 0; y < sourceRectangle.height; y += 1)
			{
				for (int x = 0; x < sourceRectangle.width; x += 1)
				{
					int sourceX = sourceRectangle.x + x;
					int sourceY = sourceRectangle.y + y;
					int targetX = xInPixel + x;
					int targetY = yInPixel + y;

					if (!targetBlock.isXYInBounds(targetX, targetY))
					{
						continue;
					}

					int character = targetBlock.getCharacter(targetX / targetBlock.getCharacterWidth(), targetY / targetBlock.getCharacterHeight());

					if (character < 0)
					{
						continue;
					}

					boolean bit = sourceBlock.getBit(sourceFont, sourceX, sourceY);
					targetBlock.setBit(targetFont, targetX, targetY, bit);
				}
			}
		}
		finally
		{
			Events.enable();
		}

		targetBlock.fireModified();
		targetFont.fireModified();

		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Pixels of block updated");
	}

	/**
	 * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
	 */
	@Override
	public void rollback()
	{
		targetFont.restore(backup);

		Registry.get(ProjectAdapter.class).getProject().setModified(true);
		Registry.get(StatusBarController.class).setMessage("Pixels of block reverted");
	}

}
