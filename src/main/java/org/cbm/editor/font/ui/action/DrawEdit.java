package org.cbm.editor.font.ui.action;

import java.util.HashSet;
import java.util.Set;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Block;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.ui.main.StatusBarController;
import org.cbm.editor.font.util.Events;

public class DrawEdit extends AbstractEdit
{

    private static final long serialVersionUID = 7223195375958171207L;

    private class Entry
    {
        private final int character;
        private final int x;
        private final int y;
        private final boolean bit;

        public Entry(int character, int x, int y, boolean bit)
        {
            super();

            this.character = character;
            this.x = x;
            this.y = y;
            this.bit = bit;
        }

        public int getCharacter()
        {
            return character;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public boolean getBit()
        {
            return bit;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            return character ^ x ^ y;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(final Object obj)
        {
            if (obj == this)
            {
                return true;
            }

            if (obj == null)
            {
                return false;
            }

            if (!(obj instanceof Entry))
            {
                return false;
            }

            final Entry entry = (Entry) obj;

            return character == entry.character && x == entry.x && y == entry.y;
        }

    }

    private final Font font;
    private final Block block;
    private final boolean bit;

    private final Set<Entry> history;

    public DrawEdit(final Font font, final Block block, final boolean bit)
    {
        super("Draw");

        this.font = font;
        this.block = block;
        this.bit = bit;

        history = new HashSet<>();
    }

    public void add(final int x, final int y)
    {
        int character = block.getCharacter(x / block.getCharacterWidth(), y / block.getCharacterHeight());

        if (character >= 0)
        {
            Entry entry = new Entry(character, x % 8, y % 8, font.getBit(character, x % 8, y % 8));

            if (!history.contains(entry))
            {
                history.add(entry);
            }
        }
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#execute()
     */
    @Override
    public void execute()
    {
        Events.disable();

        try
        {
            for (final Entry entry : history)
            {
                font.setBit(entry.getCharacter(), entry.getX(), entry.getY(), bit);
            }
        }
        finally
        {
            Events.enable();
        }

        block.fireModified();
        Registry.get(ProjectAdapter.class).getProject().setModified(true);
    }

    /**
     * @see org.cbm.editor.font.ui.action.AbstractEdit#rollback()
     */
    @Override
    public void rollback()
    {
        Events.disable();

        try
        {
            for (final Entry entry : history)
            {
                font.setBit(entry.getCharacter(), entry.getX(), entry.getY(), entry.getBit());
            }
        }
        finally
        {
            Events.enable();
        }

        block.fireModified();
        Registry.get(ProjectAdapter.class).getProject().setModified(true);
        Registry.get(StatusBarController.class).setMessage("Reverted draw");
    }

}
