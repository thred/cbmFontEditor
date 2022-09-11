package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Project;

public class ProjectSwitchedEvent extends ProjectEvent
{

    private final Project oldProject;

    public ProjectSwitchedEvent(final Project oldProject, final Project newProject)
    {
        super(newProject);

        this.oldProject = oldProject;
    }

    public Project getOldProject()
    {
        return oldProject;
    }

}
