package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Project;

public class ProjectEvent
{

    private final Project project;

    public ProjectEvent(Project project)
    {
        super();

        this.project = project;
    }

    public Project getProject()
    {
        return project;
    }

}
