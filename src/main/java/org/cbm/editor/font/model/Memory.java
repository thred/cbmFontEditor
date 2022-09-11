package org.cbm.editor.font.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.cbm.editor.font.util.Strings;
import org.cbm.editor.font.util.XMLBuilder;
import org.cbm.editor.font.util.XMLParser;

public class Memory
{

    private final byte[] memory;

    public Memory(final int size)
    {
        super();

        memory = new byte[size];

        for (int i = 0; i < size; i += 1)
        {
            memory[i] = (byte) (Math.random() * 256);
        }
    }

    private Memory(final byte[] memory)
    {
        super();

        this.memory = memory;
    }

    public Memory copy()
    {
        return new Memory(Arrays.copyOf(memory, memory.length));
    }

    public Memory restore(Memory memory)
    {
        for (int i = 0; i < memory.size(); i += 1)
        {
            this.memory[i] = memory.getByte(i);
        }

        return this;
    }

    public Memory clear()
    {
        for (int i = 0; i < size(); i += 1)
        {
            memory[i] = 0;
        }

        return this;
    }

    public Memory fill(final int index, final int length, final byte value)
    {
        defenseLength(index, length);

        for (int i = index; i < index + length; i += 1)
        {
            memory[i] = value;
        }

        return this;
    }

    public byte getByte(final int index)
    {
        defenseIndex(index);

        return memory[index];
    }

    public Memory setByte(final int index, final byte value)
    {
        if (getByte(index) == value)
        {
            return this;
        }

        memory[index] = value;

        return this;
    }

    public byte[] getBytes(final int index, final byte[] buffer)
    {
        defenseLength(index, buffer.length);

        System.arraycopy(memory, index, buffer, 0, buffer.length);

        return buffer;
    }

    public Memory setBytes(final byte[] buffer, final int offset, int length)
    {
        defenseLength(offset, buffer.length);

        System.arraycopy(buffer, 0, memory, offset, buffer.length);

        return this;
    }

    public boolean getBit(final int index, final int bit)
    {
        defenseIndex(index);
        defenseBit(bit);

        return (memory[index] & 1 << bit) > 0;
    }

    public Memory setBit(final int index, final int bit, final boolean value)
    {
        if (getBit(index, bit) == value)
        {
            return this;
        }

        final int b = 1 << bit;

        if (value)
        {
            memory[index] |= b;
        }
        else
        {
            memory[index] &= ~b;
        }

        return this;
    }

    public int size()
    {
        return memory.length;
    }

    public boolean isIndexInBounds(final int index)
    {
        return index >= 0 && index < size();
    }

    public void defenseIndex(final int index)
    {
        if (!isIndexInBounds(index))
        {
            throw new IllegalArgumentException(String.format("Index out of bounds: %d", index));
        }
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

    public boolean isBitInBounds(final int bit)
    {
        return bit >= 0 && bit < 8;
    }

    public void defenseBit(final int bit)
    {
        if (!isBitInBounds(bit))
        {
            throw new IllegalArgumentException(String.format("Invalid bit: %d", bit));
        }
    }

    public Memory load(InputStream in) throws IOException
    {
        final byte[] buffer = new byte[256];
        int length;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try
        {
            while ((length = in.read(buffer)) >= 0)
            {
                out.write(buffer, 0, length);
            }
        }
        finally
        {
            out.close();
        }

        byte[] bytes = out.toByteArray();

        if (bytes.length == size())
        {
            setBytes(bytes, 0, bytes.length);
        }
        else if (bytes.length == size() + 2)
        {
            byte[] newBytes = new byte[size()];
            System.arraycopy(bytes, 2, newBytes, 0, size());
            setBytes(newBytes, 0, newBytes.length);
        }
        else
        {
            throw new IOException("Invalid size: " + bytes.length);
        }

        return this;
    }

    public void exportMemory(final XMLBuilder builder)
    {
        builder.begin("memory");
        builder.attribute("size", memory.length);

        for (int i = 0; i < memory.length; i += 1)
        {
            if (i % 8 == 0)
            {
                builder.text("\n        ");
                builder.comment("char " + Strings.toHex((byte) (i / 8)));
                builder.text(" ");
            }

            builder.text(Strings.toHex(memory[i]));

            if (i < memory.length - 1)
            {
                builder.text(", ");
            }
        }

        builder.text("\n    ");
        builder.end();
    }

    public static Memory importMemory(final XMLParser memoryParser) throws IOException
    {
        if (memoryParser == null)
        {
            throw new IOException("Memory element missing");
        }

        final int size = memoryParser.attribute("size", -1);

        if (size <= 0)
        {
            throw new IOException("Invalid memory size: " + size);
        }

        final byte[] memory = new byte[size];
        final String text = memoryParser.text();
        final StringTokenizer tokenizer = new StringTokenizer(text, ",");

        int i = 0;

        while (tokenizer.hasMoreTokens())
        {
            if (i >= memory.length)
            {
                throw new IOException("Too many values in memory");
            }

            memory[i] = Integer.decode(tokenizer.nextToken().trim()).byteValue();

            i += 1;
        }

        if (i < memory.length)
        {
            throw new IOException("Too few values in memory");
        }

        return new Memory(memory);
    }

}
