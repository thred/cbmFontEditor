package org.cbm.editor.font.model.events;

import org.cbm.editor.font.model.Project;

public class ProjectPropertyModifiedEvent extends ProjectEvent
{

    private final String propertyName;
    private final Object value;

    public ProjectPropertyModifiedEvent(final Project project, final String propertyName, final Object value)
    {
        super(project);

        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public Object getValue()
    {
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.cbm.editor.font.model.events.ProjectEvent#toString()
     */
    @Override
    public String toString()
    {
        return String.format("%s(propertyName=%s, value=%s", getClass().getSimpleName(), getPropertyName(),
            String.valueOf(getValue()));
    }

}
