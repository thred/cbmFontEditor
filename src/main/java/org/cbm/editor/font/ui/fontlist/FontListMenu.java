package org.cbm.editor.font.ui.fontlist;

import java.awt.Component;

import javax.swing.JPopupMenu;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.ui.action.CopyAction;
import org.cbm.editor.font.ui.action.CutAction;
import org.cbm.editor.font.ui.action.PasteAction;

public class FontListMenu extends JPopupMenu
{

    private static final long serialVersionUID = -7348948975571399612L;

    //	public class State
    //	{
    //		private final AbstractBlockComponent blockComponent;
    //		private final Point position;
    //		private final OldBlockLayer layer;
    //		private final Point characterPosition;
    //		private final int character;
    //
    //		public State(final Component component, final Point point)
    //		{
    //			super();
    //
    //			blockComponent = (AbstractBlockComponent) component;
    //			position = blockComponent.convertFromComponentToBlock(point);
    //			layer = blockComponent.getLayer().get(position.x, position.y);
    //			characterPosition =
    //			    new Point(position.x / layer.getCharacterWidth(), position.y / layer.getCharacterHeight());
    //			character = layer.getCharacter(characterPosition.x, characterPosition.y);
    //		}
    //
    //		public AbstractBlockComponent getBlockComponent()
    //		{
    //			return blockComponent;
    //		}
    //
    //		public Point getPosition()
    //		{
    //			return position;
    //		}
    //
    //		public OldBlockLayer getLayer()
    //		{
    //			return layer;
    //		}
    //
    //		public Point getCharacterPosition()
    //		{
    //			return characterPosition;
    //		}
    //
    //		public int getCharacter()
    //		{
    //			return character;
    //		}
    //
    //	}

    //	protected State state;

    public FontListMenu()
    {
        super();

        add(Registry.get(CutAction.class));
        add(Registry.get(CopyAction.class));
        add(Registry.get(PasteAction.class));
    }

    @Override
    public void show(final Component invoker, final int x, final int y)
    {
        //		state = new State(invoker, new Point(x, y));
        //
        //		for (final JMenuItem item : foregroundColorItems)
        //		{
        //			item.setEnabled(state.getCharacter() >= 0);
        //		}

        super.show(invoker, x, y);
    }

}
