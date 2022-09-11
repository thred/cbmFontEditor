package org.cbm.editor.font.model;

import java.io.IOException;
import java.io.InputStream;

import org.cbm.editor.font.model.events.FontBitModifiedEvent;
import org.cbm.editor.font.model.events.FontByteModifiedEvent;
import org.cbm.editor.font.model.events.FontCharactersModifiedEvent;
import org.cbm.editor.font.model.events.FontModifiedEvent;
import org.cbm.editor.font.model.events.FontPropertyModifiedEvent;
import org.cbm.editor.font.util.Events;
import org.cbm.editor.font.util.Objects;
import org.cbm.editor.font.util.XMLBuilder;
import org.cbm.editor.font.util.XMLParser;

public class Font
{

    private final Memory memory;
    private final Block block;

    private String name;

    public Font()
    {
        this("unnamed", getDefaultMemory());
    }

    public Font(String name, Memory memory)
    {
        super();

        if (memory.size() != 0x0800)
        {
            throw new IllegalArgumentException("Invalid size of memory: " + memory.size());
        }

        this.name = name;
        this.memory = memory;

        block = Block.createDefault().setEditable(false);
    }

    public Font copy()
    {
        return new Font(name, memory.copy());
    }

    public Font restore(Font font)
    {
        name = font.getName();
        memory.restore(font.getMemory());

        Events.fire(this, new FontModifiedEvent(this));

        return this;
    }

    public Font setName(String name)
    {
        if (!Objects.equals(this.name, name))
        {
            this.name = name;

            Events.fire(this, new FontPropertyModifiedEvent(this, "name", name));
        }

        return this;
    }

    public String getName()
    {
        return name;
    }

    public Memory getMemory()
    {
        return memory;
    }

    public Block getBlock()
    {
        return block;
    }

    public Font load(final InputStream in) throws IOException
    {
        memory.load(in);

        return this;
    }

    public byte getByte(final int character, final int y)
    {
        defenseCharacter(character);
        defenseY(y);

        return getMemory().getByte(character * 8 + y);
    }

    public Font setByte(final int character, final int y, final byte value)
    {
        if (getByte(character, y) == value)
        {
            return this;
        }

        getMemory().setByte(character * 8 + y, value);

        Events.fire(this, new FontByteModifiedEvent(this, character, y));

        return this;
    }

    public byte[] getBytes(final int index, final byte[] buffer)
    {
        defenseLength(index, buffer.length);

        return getMemory().getBytes(index, buffer);
    }

    public Font setBytes(final int index, final byte[] buffer)
    {
        defenseLength(index, buffer.length);

        getMemory().setBytes(buffer, index, buffer.length);

        Events.fire(this, new FontCharactersModifiedEvent(this, FontCharactersModifiedEvent.ALL));

        return this;
    }

    public boolean getBit(final int character, final int x, final int y)
    {
        defenseCharacter(character);
        defenseXY(x, y);

        return getMemory().getBit(character * 8 + y, 7 - x);
    }

    public Font setBit(final int character, final int x, final int y, final boolean value)
    {
        if (getBit(character, x, y) == value)
        {
            return this;
        }

        getMemory().setBit(character * 8 + y, 7 - x, value);

        Events.fire(this, new FontBitModifiedEvent(this, character, x, y));

        return this;
    }

    public int size()
    {
        return 2048;
    }

    public Font fireModified()
    {
        Events.fire(this, new FontModifiedEvent(this));

        return this;
    }

    public boolean isCharacterInBounds(final int character)
    {
        return character >= 0 && character < 256;
    }

    protected void defenseCharacter(final int character)
    {
        if (!isCharacterInBounds(character))
        {
            throw new IllegalArgumentException(String.format("Invalid character: %d", character));
        }
    }

    public boolean isXInBounds(final int x)
    {
        return x >= 0 && x < 8;
    }

    public boolean isYInBounds(final int y)
    {
        return y >= 0 && y < 8;
    }

    public boolean isXYInBounds(final int x, final int y)
    {
        return isXInBounds(x) && isYInBounds(y);
    }

    protected void defenseX(final int x)
    {
        if (!isXInBounds(x))
        {
            throw new IllegalArgumentException(String.format("Invalid value for x: %d", x));
        }
    }

    protected void defenseY(final int y)
    {
        if (!isYInBounds(y))
        {
            throw new IllegalArgumentException(String.format("Invalid value for y: %d", y));
        }
    }

    protected void defenseXY(final int x, final int y)
    {
        defenseX(x);
        defenseY(y);
    }

    public boolean isLengthInBounds(final int index, final int length)
    {
        return index >= 0 && length >= 0 && index + length <= size();
    }

    public void defenseLength(final int index, final int length)
    {
        if (!isLengthInBounds(index, length))
        {
            throw new IllegalArgumentException(
                String.format("Length out of bounds: %d + %d = %d", index, length, index + length));
        }
    }

    public void exportFont(final XMLBuilder builder)
    {
        builder.begin("font");
        builder.attribute("name", name);

        memory.exportMemory(builder);

        builder.end();
    }

    /**
     * {@inheritDoc}
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return name;
    }

    public static Font importFont(final XMLParser fontParser) throws IOException
    {
        if (fontParser == null)
        {
            throw new IOException("Font element missing");
        }

        String name = fontParser.attribute("name", "default");
        Memory memory = Memory.importMemory(fontParser.into("memory"));

        return new Font(name, memory);
    }

    private static Memory getDefaultMemory()
    {
        Memory memory = new Memory(0x0800);

        final InputStream in = Font.class.getClassLoader().getResourceAsStream("org/cbm/editor/default.fnt");

        try
        {
            try
            {
                memory.load(in);
            }
            finally
            {
                in.close();
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        return memory;
    }

}
