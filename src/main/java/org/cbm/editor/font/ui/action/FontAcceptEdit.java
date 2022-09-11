package org.cbm.editor.font.ui.action;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.block.BlockSelection;
import org.cbm.editor.font.ui.main.StatusBarController;

public class FontAcceptEdit extends AbstractEdit
{

    private static final long serialVersionUID = -2012377693460397308L;

    private final Font font;
    private final Block block;
    private final int xInCharacter;
    private final int yInCharacter;
    private final BlockSelection blockSelection;

    private Font backup;

    public FontAcceptEdit(Font font, Block block, int xInCharacter, int yInCharacter, BlockSelection blockSelection)
    {
        super("Accept");

        this.font = font;
        this.block = block;
        this.xInCharacter = xInCharacter;
        this.yInCharacter = yInCharacter;
        this.blockSelection = blockSelection;
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        backup = font.copy();
        Font sourceFont = blockSelection.getFont();
        Block sourceBlock = blockSelection.getBlock();

        for (int y = 0; y < sourceBlock.getHeightInCharacters(); y += 1)
        {
            for (int x = 0; x < sourceBlock.getWidthInCharacters(); x += 1)
            {
                if (!block.isCharacterXYInBounds(xInCharacter + x, yInCharacter + y))
                {
                    continue;
                }

                int character = block.getCharacter(xInCharacter + x, yInCharacter + y);

                if (character < 0)
                {
                    continue;
                }

                byte[] bytes = sourceFont.getBytes(sourceBlock.getCharacter(x, y) * 8, new byte[8]);

                font.setBytes(character * 8, bytes);
            }
        }

        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Characters of block updated");
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        font.restore(backup);

        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Characters of font reverted");
    }

}
