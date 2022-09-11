package org.cbm.editor.font.ui.block;

import java.awt.Component;
import java.awt.Point;
import java.util.Collection;

import org.cbm.editor.font.model.Block;

public class BlockState
{
    private final BlockComponent blockComponent;
    private final Point positionInPixel;
    private final Block block;
    private final Collection<Point> positionsInCharacter;

    public BlockState(final Component component, final Point positionInComponent)
    {
        super();

        blockComponent = (BlockComponent) component;
        positionInPixel = blockComponent.convertFromComponentToPixel(positionInComponent);
        block = blockComponent.getRootLayer().getBlock();
        positionsInCharacter = blockComponent.getCharacterPositions(positionInPixel);
    }

    public BlockComponent getBlockComponent()
    {
        return blockComponent;
    }

    public Point getPositionInPixel()
    {
        return positionInPixel;
    }

    public Block getBlock()
    {
        return block;
    }

    public Collection<Point> getPositionsInCharacter()
    {
        return positionsInCharacter;
    }

    public boolean isBackground()
    {
        for (Point position : positionsInCharacter)
        {
            if (block.isCharacterXYInBounds(position.x, position.y) && block.getCharacter(position.x, position.y) >= 0)
            {
                return false;
            }
        }

        return true;
    }

}
