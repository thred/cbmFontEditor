package org.cbm.editor.font.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cbm.editor.font.model.events.BlockAddedEvent;
import org.cbm.editor.font.model.events.BlockMovedEvent;
import org.cbm.editor.font.model.events.BlockRemovedEvent;
import org.cbm.editor.font.model.events.FontAddedEvent;
import org.cbm.editor.font.model.events.FontMovedEvent;
import org.cbm.editor.font.model.events.FontRemovedEvent;
import org.cbm.editor.font.model.events.ProjectPropertyModifiedEvent;
import org.cbm.editor.font.util.Events;
import org.cbm.editor.font.util.Objects;
import org.cbm.editor.font.util.XMLBuilder;
import org.cbm.editor.font.util.XMLParser;

public class Project
{

    private final List<Font> fonts;
    private final List<Block> blocks;

    private File file;
    private boolean modified;

    public Project()
    {
        super();

        fonts = new ArrayList<>();
        blocks = new ArrayList<>();
    }

    private Project(List<Font> fonts, List<Block> blocks)
    {
        super();

        this.fonts = fonts;
        this.blocks = blocks;
    }

    public Project addFont(int index, Font font)
    {
        fonts.add(index, font);

        Events.fire(this, new FontAddedEvent(font, index));

        return this;
    }

    public int getNumberOfFonts()
    {
        return fonts.size();
    }

    public Font getFont(int index)
    {
        if (index < 0 || index >= fonts.size())
        {
            return null;
        }

        return fonts.get(index);
    }

    public int removeFont(Font font)
    {
        int index = fonts.indexOf(font);

        fonts.remove(font);

        Events.fire(this, new FontRemovedEvent(font, index));

        return index;
    }

    public int moveFont(int index, Font font)
    {
        int previousIndex = fonts.indexOf(font);

        if (previousIndex == index)
        {
            return previousIndex;
        }

        fonts.remove(font);
        fonts.add(index, font);

        Events.fire(this, new FontMovedEvent(font, index, previousIndex));

        return previousIndex;
    }

    public int indexOfFont(Font font)
    {
        return fonts.indexOf(font);
    }

    public Project addBlock(int index, Block block)
    {
        blocks.add(index, block);

        Events.fire(this, new BlockAddedEvent(block, index));

        return this;
    }

    public int getNumberOfBlocks()
    {
        return blocks.size();
    }

    public Block getBlock(int index)
    {
        if (index < 0 || index >= blocks.size())
        {
            return null;
        }

        return blocks.get(index);
    }

    public int removeBlock(Block block)
    {
        int index = blocks.indexOf(block);

        blocks.remove(block);

        Events.fire(this, new BlockRemovedEvent(block, index));

        return index;
    }

    public int moveBlock(int index, Block block)
    {
        int previousIndex = blocks.indexOf(block);

        if (previousIndex == index)
        {
            return previousIndex;
        }

        blocks.remove(block);
        blocks.add(index, block);

        Events.fire(this, new BlockMovedEvent(block, index, previousIndex));

        return previousIndex;
    }

    public int indexOfBlock(Block block)
    {
        return blocks.indexOf(block);
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        if (!Objects.equals(this.file, file))
        {
            this.file = file;

            Events.fire(this, new ProjectPropertyModifiedEvent(this, "file", file));
        }
    }

    public boolean isModified()
    {
        return modified;
    }

    public void setModified(boolean modified)
    {
        if (!Objects.equals(this.modified, modified))
        {
            this.modified = modified;

            Events.fire(this, new ProjectPropertyModifiedEvent(this, "modified", modified));
        }
    }

    public void exportProject(File file) throws IOException
    {
        XMLBuilder builder = new XMLBuilder();

        builder.begin("cbm-font-editor-project");
        builder.begin("fonts");

        for (Font font : fonts)
        {
            font.exportFont(builder);
        }

        builder.end();
        builder.begin("blocks");

        for (Block block : blocks)
        {
            block.exportBlock(builder);
        }

        builder.end();
        builder.end();
        builder.toFile(file);
    }

    public static Project importProject(File file) throws IOException
    {
        XMLParser parser = new XMLParser(file);

        return importProject(parser.into("cbm-font-editor-project"));
    }

    private static Project importProject(XMLParser projectParser) throws IOException
    {
        Events.disable();

        try
        {
            if (projectParser == null)
            {
                throw new IOException("Document element missing");
            }

            List<Font> fonts = new ArrayList<>();

            for (XMLParser parser : projectParser.into("fonts").iterator("font"))
            {
                fonts.add(Font.importFont(parser));
            }

            List<Block> blocks = new ArrayList<>();

            for (XMLParser parser : projectParser.into("blocks").iterator("block"))
            {
                blocks.add(Block.importBlock(parser));
            }

            return new Project(fonts, blocks);
        }
        finally
        {
            Events.enable();
        }
    }

}
