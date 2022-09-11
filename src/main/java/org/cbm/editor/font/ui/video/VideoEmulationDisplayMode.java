package org.cbm.editor.font.ui.video;

import java.util.ResourceBundle;

public enum VideoEmulationDisplayMode
{

    LUMINANCE_CHANNEL(),

    CHROMINANCE_CHANNEL(),

    CHROMINANCE_I_CHANNEL(),

    CHROMINANCE_Q_CHANNEL(),

    BLACK_AND_WHITE_TV(),

    MONITOR(),

    TV_SET();

    private final String name;
    private final String description;

    private VideoEmulationDisplayMode()
    {
        final ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName().replace('.', '/'));

        name = bundle.getString(name() + ".name");
        description = bundle.getString(name() + ".description");
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return getName();
    }

}
