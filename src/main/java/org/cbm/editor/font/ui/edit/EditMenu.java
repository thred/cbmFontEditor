package org.cbm.editor.font.ui.edit;

import java.awt.Component;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.ui.action.BlockColorAction;
import org.cbm.editor.font.ui.action.CopyAction;
import org.cbm.editor.font.ui.action.CutAction;
import org.cbm.editor.font.ui.action.PasteAction;
import org.cbm.editor.font.ui.block.BlockComponent;
import org.cbm.editor.font.ui.block.BlockState;
import org.cbm.editor.font.util.Palette;

public class EditMenu extends JPopupMenu
{

    private static final long serialVersionUID = -7348948975571399612L;

    private final List<BlockColorAction> colorActions;

    public EditMenu()
    {
        super();

        colorActions = new ArrayList<>();

        for (final Palette color : Palette.values())
        {
            BlockColorAction action = new BlockColorAction(color);

            colorActions.add(action);
            add(action);
        }

        addSeparator();

        add(Registry.get(CutAction.class));
        add(Registry.get(CopyAction.class));
        add(Registry.get(PasteAction.class));
    }

    @Override
    public void show(final Component invoker, final int x, final int y)
    {
        BlockComponent blockComponent = (BlockComponent) invoker;
        BlockState state = new BlockState(invoker, blockComponent.transfer(new Point(x, y)));

        for (final BlockColorAction action : colorActions)
        {
            action.setState(state);
        }

        super.show(invoker, x, y);
    }

}
