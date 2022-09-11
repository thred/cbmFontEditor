package org.cbm.editor.font.model;

import java.util.Collection;
import java.util.HashSet;

import org.cbm.editor.font.model.events.ProjectSwitchedEvent;
import org.cbm.editor.font.util.Events;

public class ProjectAdapter
{

    private final Collection<Object> consumers;

    private Project project;

    public ProjectAdapter()
    {
        super();

        consumers = new HashSet<>();
    }

    public ProjectAdapter bind(Object consumer)
    {
        synchronized (consumers)
        {
            consumers.add(consumer);

            Events.bind(this, consumer);

            if (project != null)
            {
                Events.bind(project, consumer);
            }
        }

        return this;
    }

    public ProjectAdapter unbind(Object consumer)
    {
        synchronized (consumers)
        {
            consumers.remove(consumer);

            Events.unbind(this, consumer);

            if (project != null)
            {
                Events.unbind(project, consumer);
            }
        }

        return this;
    }

    public Project getProject()
    {
        return project;
    }

    public ProjectAdapter setProject(final Project project)
    {
        synchronized (consumers)
        {
            if (this.project == project)
            {
                return this;
            }

            for (Object consumer : consumers)
            {
                if (this.project != null)
                {
                    Events.unbind(this.project, consumer);
                }

                if (project != null)
                {
                    Events.bind(project, consumer);
                }
            }

            final ProjectSwitchedEvent event = new ProjectSwitchedEvent(this.project, project);

            this.project = project;

            Events.fire(this, event);
        }

        return this;
    }

}
