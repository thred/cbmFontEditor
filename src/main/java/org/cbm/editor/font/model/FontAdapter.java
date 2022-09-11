package org.cbm.editor.font.model;

import java.util.Collection;
import java.util.HashSet;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.events.FontRemovedEvent;
import org.cbm.editor.font.model.events.FontSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectEvent;
import org.cbm.editor.font.util.Events;

public class FontAdapter
{

    private final Collection<Object> consumers;
    private final ProjectAdapter projectAdapter;

    private Font font;

    public FontAdapter()
    {
        super();

        consumers = new HashSet<>();
        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
    }

    public void handleEvent(ProjectEvent event)
    {
        if (projectAdapter.getProject() == null)
        {
            setFont(null);
        }
    }

    public void handleEvent(FontRemovedEvent event)
    {
        if (font == event.getFont())
        {
            setFont(null);
        }
    }

    public FontAdapter bind(Object consumer)
    {
        synchronized (consumers)
        {
            consumers.add(consumer);

            Events.bind(this, consumer);

            if (font != null)
            {
                Events.bind(font, consumer);
            }
        }

        return this;
    }

    public FontAdapter unbind(Object consumer)
    {
        synchronized (consumers)
        {
            consumers.remove(consumer);

            Events.unbind(this, consumer);

            if (font != null)
            {
                Events.unbind(font, consumer);
            }
        }

        return this;
    }

    public Font getFont()
    {
        return font;
    }

    public FontAdapter setFont(Font font)
    {
        synchronized (consumers)
        {
            if (this.font == font)
            {
                return this;
            }

            for (Object consumer : consumers)
            {
                if (this.font != null)
                {
                    Events.unbind(this.font, consumer);
                }

                if (font != null)
                {
                    Events.bind(font, consumer);
                }
            }

            FontSwitchedEvent event = new FontSwitchedEvent(this.font, font);

            this.font = font;

            Events.fire(this, event);
        }

        return this;
    }

}