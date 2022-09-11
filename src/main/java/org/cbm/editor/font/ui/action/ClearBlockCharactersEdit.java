package org.cbm.editor.font.ui.action;

import java.awt.Rectangle;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Events;

public class ClearBlockCharactersEdit extends AbstractEdit
{

    private static final long serialVersionUID = 4512102909605032035L;

    private final Block block;
    private final Rectangle rectangle;

    private Block backup;

    public ClearBlockCharactersEdit(Block block, Rectangle rectangle)
    {
        super("Clear Block's Characters");

        this.block = block;
        this.rectangle = rectangle;
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        backup = block.copy();

        Events.disable();

        try
        {
            for (int y = 0; y < rectangle.height; y += 1)
            {
                for (int x = 0; x < rectangle.width; x += 1)
                {
                    final int characterX = rectangle.x + x;
                    final int characterY = rectangle.y + y;

                    if (block.isCharacterXYInBounds(characterX, characterY))
                    {
                        block.setCharacter(characterX, characterY, -1);
                    }
                }
            }
        }
        finally
        {
            Events.enable();
        }

        block.pack();
        block.fireModified();
        Registry.get(ProjectAdapter.class).getProject().setModified(true);
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        block.restore(backup);
        block.fireModified();
        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Reverted clear block's characters");
    }

}
