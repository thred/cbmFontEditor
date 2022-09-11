package org.cbm.editor.font.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.cbm.editor.font.model.events.BlockBitModifiedEvent;
import org.cbm.editor.font.model.events.BlockCharacterModifiedEvent;
import org.cbm.editor.font.model.events.BlockModifiedEvent;
import org.cbm.editor.font.model.events.BlockPropertyModifiedEvent;
import org.cbm.editor.font.util.Events;
import org.cbm.editor.font.util.Objects;
import org.cbm.editor.font.util.Palette;
import org.cbm.editor.font.util.Strings;
import org.cbm.editor.font.util.XMLBuilder;
import org.cbm.editor.font.util.XMLParser;

public class Block
{

    private String name;
    private int widthInCharacters;
    private int heightInCharacters;
    private int[] chars;
    private Palette[] foregrounds;
    private Palette background = Palette.BLUE;
    private boolean editable;

    public Block(final int widthInCharacters, final int heightInCharacters)
    {
        super();

        name = "unnamed";
        this.widthInCharacters = widthInCharacters;
        this.heightInCharacters = heightInCharacters;

        chars = new int[widthInCharacters * heightInCharacters];
        foregrounds = new Palette[widthInCharacters * heightInCharacters];

        Arrays.fill(chars, -1);

        editable = true;
    }

    public Block(final Block block)
    {
        super();

        name = block.getName();
        widthInCharacters = block.getWidthInCharacters();
        heightInCharacters = block.getHeightInCharacters();
        chars = Arrays.copyOf(block.chars, block.chars.length);
        foregrounds = Arrays.copyOf(block.foregrounds, block.foregrounds.length);
        background = block.getBackground();
        editable = block.isEditable();
    }

    private Block(final String name, final int widthInCharacters, final int heightInCharacters, final int[] chars,
        final Palette[] foregrounds, final Palette background, boolean editable)
    {
        super();

        this.name = name;
        this.widthInCharacters = widthInCharacters;
        this.heightInCharacters = heightInCharacters;
        this.chars = chars;
        this.foregrounds = foregrounds;
        this.background = background;
        this.editable = editable;
    }

    public Block copy()
    {
        return new Block(name, widthInCharacters, heightInCharacters, Arrays.copyOf(chars, chars.length),
            Arrays.copyOf(foregrounds, foregrounds.length), background, editable);
    }

    public Block copy(int xInCharacter, int yInCharacter, int widthInCharacter, int heightInCharacter)
    {
        int[] copiedChars = new int[widthInCharacter * heightInCharacter];
        Palette[] copiedForegrounds = new Palette[widthInCharacter * heightInCharacter];

        for (int y = 0; y < heightInCharacter; y += 1)
        {
            for (int x = 0; x < widthInCharacter; x += 1)
            {
                int targetIndex = x + y * widthInCharacter;
                int sourceX = x + xInCharacter;
                int sourceY = y + yInCharacter;

                if (isXYInBounds(sourceX, sourceY))
                {
                    copiedChars[targetIndex] = getCharacter(sourceX, sourceY);
                    copiedForegrounds[targetIndex] = getForeground(sourceX, sourceY);
                }
                else
                {
                    copiedChars[targetIndex] = -1;
                    copiedForegrounds[targetIndex] = null;
                }
            }
        }

        return new Block(name, widthInCharacter, heightInCharacter, copiedChars, copiedForegrounds, background,
            editable);
    }

    public Block restore(Block block)
    {
        name = block.getName();
        widthInCharacters = block.getWidthInCharacters();
        heightInCharacters = block.getHeightInCharacters();
        chars = Arrays.copyOf(block.chars, block.chars.length);
        foregrounds = Arrays.copyOf(block.foregrounds, block.foregrounds.length);
        background = block.getBackground();
        editable = block.isEditable();

        fireModified();

        return this;
    }

    public String getName()
    {
        return name;
    }

    public Block setName(final String name)
    {
        if (!Objects.equals(this.name, name))
        {
            this.name = name;

            Events.fire(this, new BlockPropertyModifiedEvent(this, "name", name));
        }

        return this;
    }

    public int getWidthInCharacters()
    {
        return widthInCharacters;
    }

    public int getHeightInCharacters()
    {
        return heightInCharacters;
    }

    public int getCharacterWidth()
    {
        return 8;
    }

    public int getCharacterHeight()
    {
        return 8;
    }

    public int getWidth()
    {
        return widthInCharacters * getCharacterWidth();
    }

    public int getHeight()
    {
        return heightInCharacters * getCharacterHeight();
    }

    public int getCharacter(final int characterX, final int characterY)
    {
        defenseCharacterXY(characterX, characterY);

        return chars[characterX + characterY * widthInCharacters];
    }

    public Block setCharacter(final int characterX, final int characterY, final int character)
    {
        if (getCharacter(characterX, characterY) == character)
        {
            return this;
        }

        defenseCharacter(character);

        chars[characterX + characterY * widthInCharacters] = character;

        Events.fire(this, new BlockCharacterModifiedEvent(this, characterX, characterY));

        return this;
    }

    public Palette getForeground(final int characterX, final int characterY)
    {
        defenseCharacterXY(characterX, characterY);

        return foregrounds[characterX + characterY * widthInCharacters];
    }

    public Block setForeground(final int characterX, final int characterY, final Palette color)
    {
        defenseCharacterXY(characterX, characterY);

        if (getForeground(characterX, characterY) == color)
        {
            return this;
        }

        foregrounds[characterX + characterY * widthInCharacters] = color;

        Events.fire(this, new BlockCharacterModifiedEvent(this, characterX, characterY));

        return this;
    }

    public Palette getBackground()
    {
        return background;
    }

    public Block setBackground(final Palette background)
    {
        if (getBackground() == background)
        {
            return this;
        }

        this.background = background;

        Events.fire(this, new BlockModifiedEvent(this));

        return this;
    }

    public boolean getBit(Font font, final int x, final int y)
    {
        defenseXY(x, y);

        final int character = getCharacter(x / getCharacterWidth(), y / getCharacterHeight());

        if (character < 0)
        {
            return false;
        }

        return font.getBit(character, x % getCharacterWidth(), y % getCharacterHeight());
    }

    public Block setBit(Font font, final int x, final int y, final boolean value)
    {
        if (getBit(font, x, y) == value)
        {
            return this;
        }

        final int character = getCharacter(x / getCharacterWidth(), y / getCharacterHeight());

        if (character < 0)
        {
            return this;
        }

        font.setBit(character, x % getCharacterWidth(), y % getCharacterHeight(), value);

        Events.fire(this, new BlockBitModifiedEvent(this, x, y));

        return this;
    }

    public Palette getColor(Font font, final int x, final int y)
    {
        if (getBit(font, x, y))
        {
            return getForeground(x / getCharacterWidth(), y / getCharacterHeight());
        }

        return getBackground();
    }

    public boolean isEditable()
    {
        return editable;
    }

    public Block setEditable(boolean editable)
    {
        if (isEditable() == editable)
        {
            return this;
        }

        this.editable = editable;

        Events.fire(this, new BlockPropertyModifiedEvent(this, "editable", editable));

        return this;
    }

    public Block paste(final int characterX, final int characterY, final Block block)
    {
        final int minimumX = Math.min(0, characterX);
        final int minimumY = Math.min(0, characterY);
        final int maximumX = Math.max(getWidthInCharacters(), characterX + block.getWidthInCharacters());
        final int maximumY = Math.max(getHeightInCharacters(), characterY + block.getHeightInCharacters());

        Events.disable();

        try
        {
            resize(-minimumX, -minimumY, maximumX - minimumX, maximumY - minimumY);

            for (int y = 0; y < block.getHeightInCharacters(); y += 1)
            {
                for (int x = 0; x < block.getWidthInCharacters(); x += 1)
                {
                    final int toX = characterX >= 0 ? characterX + x : x;
                    final int toY = characterY >= 0 ? characterY + y : y;

                    setCharacter(toX, toY, block.getCharacter(x, y));
                    setForeground(toX, toY, block.getForeground(x, y));
                }
            }
        }
        finally
        {
            Events.enable();
        }

        return fireModified();
    }

    public Block pack()
    {
        int minimumX = getWidthInCharacters();
        int minimumY = getHeightInCharacters();
        int maximumX = 0;
        int maximumY = 0;

        for (int y = 0; y < getHeightInCharacters(); y += 1)
        {
            for (int x = 0; x < getWidthInCharacters(); x += 1)
            {
                if (getCharacter(x, y) >= 0)
                {
                    minimumX = Math.min(minimumX, x);
                    minimumY = Math.min(minimumY, y);
                    maximumX = Math.max(maximumX, x);
                    maximumY = Math.max(maximumY, y);
                }
            }
        }

        minimumX = Math.min(minimumX, maximumX);
        minimumY = Math.min(minimumY, maximumY);

        if (minimumX > 0 || minimumY > 0 || maximumX < getWidthInCharacters() || maximumY < getHeightInCharacters())
        {
            resize(minimumX, minimumY, maximumX - minimumX, maximumY - minimumY);
        }

        return this;
    }

    public Block resize(final int characterX, final int characterY, final int widthInCharacters,
        final int heightInCharacters)
    {
        Events.disable();

        try
        {
            if (characterX != 0
                || characterY != 0
                || this.widthInCharacters != widthInCharacters
                || this.heightInCharacters != heightInCharacters)
            {
                final int[] chars = new int[widthInCharacters * heightInCharacters];
                final Palette[] foregrounds = new Palette[widthInCharacters * heightInCharacters];

                Arrays.fill(chars, -1);

                for (int y = 0; y < this.heightInCharacters; y += 1)
                {
                    for (int x = 0; x < this.widthInCharacters; x += 1)
                    {
                        final int toX = x + characterX;
                        final int toY = y + characterY;

                        if (toX >= 0 && toY >= 0 && toX < widthInCharacters && toY < heightInCharacters)
                        {
                            chars[toX + widthInCharacters * toY] = getCharacter(x, y);
                            foregrounds[toX + widthInCharacters * toY] = getForeground(x, y);
                        }
                    }
                }

                this.widthInCharacters = widthInCharacters;
                this.heightInCharacters = heightInCharacters;
                this.chars = chars;
                this.foregrounds = foregrounds;
            }
        }
        finally
        {
            Events.enable();
        }

        return fireModified();
    }

    public Block fireModified()
    {
        Events.fire(this, new BlockModifiedEvent(this));

        return this;
    }

    public boolean isXYInBounds(final int x, final int y)
    {
        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    public void defenseXY(final int x, final int y)
    {
        if (!isXYInBounds(x, y))
        {
            throw new IllegalArgumentException(String.format("Position out of bounds: %d, %d", x, y));
        }
    }

    public boolean isCharacterXYInBounds(final int characterX, final int characterY)
    {
        return characterX >= 0 && characterY >= 0 && characterX < widthInCharacters && characterY < heightInCharacters;
    }

    public void defenseCharacterXY(final int characterX, final int characterY)
    {
        if (!isCharacterXYInBounds(characterX, characterY))
        {
            throw new IllegalArgumentException(
                String.format("Character position out of bounds: %d, %d", characterX, characterY));
        }
    }

    public boolean isCharacterInBounds(final int character)
    {
        return character == -1 || character >= 0 && character < 256;
    }

    protected void defenseCharacter(final int character)
    {
        if (!isCharacterInBounds(character))
        {
            throw new IllegalArgumentException(String.format("Invalid character: %d", character));
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getName();
    }

    public static Block createDefault()
    {
        final Block result = new Block(16, 16);

        Events.disable();

        try
        {
            result.setBackground(Palette.BLUE);

            for (int y = 0; y < 16; y += 1)
            {
                for (int x = 0; x < 16; x += 1)
                {
                    result.setCharacter(x, y, x + y * 16);
                    result.setForeground(x, y, Palette.LIGHT_BLUE);
                }
            }
        }
        finally
        {
            Events.enable();
        }

        return result;
    }

    public static Block create(final String name, final int width, final int height, final Palette foreground,
        final Palette background, final int... characters)
    {
        final Block result = new Block(width, height);

        Events.disable();

        try
        {
            result.setName(name);
            result.setBackground(background);

            for (int y = 0; y < height; y += 1)
            {
                for (int x = 0; x < width; x += 1)
                {
                    result.setCharacter(x, y, characters[x + y * width]);
                    result.setForeground(x, y, foreground);
                }
            }
        }
        finally
        {
            Events.enable();
        }

        return result;
    }

    public Block exportBlock(final XMLBuilder builder)
    {
        builder.begin("block");
        builder.attribute("name", name);
        builder.attribute("width-in-characters", widthInCharacters);
        builder.attribute("height-in-characters", heightInCharacters);
        builder.attribute("background", background);
        builder.attribute("editable", editable);

        builder.begin("chars");

        for (int i = 0; i < chars.length; i += 1)
        {
            if (i % widthInCharacters == 0)
            {
                builder.text("\n                ");
            }

            builder.text(chars[i] >= 0 ? Strings.toHex((byte) chars[i]) : "null");

            if (i < chars.length - 1)
            {
                builder.text(", ");
            }
        }

        builder.text("\n            ");
        builder.end();

        builder.begin("foregrounds");

        for (int i = 0; i < foregrounds.length; i += 1)
        {
            if (i % widthInCharacters == 0)
            {
                builder.text("\n                ");
            }

            builder.text(String.format("%11s", String.valueOf(foregrounds[i])));

            if (i < foregrounds.length - 1)
            {
                builder.text(", ");
            }
        }

        builder.text("\n            ");
        builder.end();
        builder.end();

        return this;
    }

    public static Block importBlock(final XMLParser blockParser) throws IOException
    {
        final String name = blockParser.attribute("name", "unknown");
        final int widthInCharacters = blockParser.attribute("width-in-characters", 0);
        final int heightInCharacters = blockParser.attribute("height-in-characters", 0);
        final Palette background = Palette.valueOf(blockParser.attribute("background", null));
        final boolean editable = blockParser.attribute("editable", true);
        final String charsText = blockParser.textOf("chars");
        final String foregroundsText = blockParser.textOf("foregrounds");

        final int[] chars = new int[widthInCharacters * heightInCharacters];
        final StringTokenizer charsTokenizer = new StringTokenizer(charsText, ",");

        int i = 0;

        while (charsTokenizer.hasMoreTokens())
        {
            if (i >= chars.length)
            {
                throw new IOException("Too many values in chars of block " + name);
            }

            String token = charsTokenizer.nextToken().trim();

            chars[i] = "null".equals(token) ? -1 : Integer.decode(token).intValue();

            i += 1;
        }

        if (i < chars.length)
        {
            throw new IOException("Too few values in chars of block " + name);
        }

        final Palette[] foregrounds = new Palette[widthInCharacters * heightInCharacters];
        final StringTokenizer foregroundsTokenizer = new StringTokenizer(foregroundsText, ",");

        i = 0;

        while (foregroundsTokenizer.hasMoreTokens())
        {
            if (i >= foregrounds.length)
            {
                throw new IOException("Too many values in foregrounds of block " + name);
            }

            String token = foregroundsTokenizer.nextToken().trim();

            foregrounds[i] = "null".equals(token) ? null : Palette.valueOf(token);

            i += 1;
        }

        if (i < foregrounds.length)
        {
            throw new IOException("Too few values in foregrounds of block " + name);
        }

        return new Block(name, widthInCharacters, heightInCharacters, chars, foregrounds, background, editable);
    }

}
