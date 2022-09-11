package org.cbm.editor.font.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.cbm.editor.font.Icon;
import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Font;
import org.cbm.editor.font.model.FontAdapter;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.FontEvent;
import org.cbm.editor.font.model.events.ProjectEvent;

public class PreviousFontAction extends AbstractAction
{

    private static final long serialVersionUID = -773527153869372460L;

    private final ProjectAdapter projectAdapter;
    private final FontAdapter fontAdapter;

    public PreviousFontAction()
    {
        super("Previous Font", Icon.LEFT.getIcon());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        fontAdapter = Registry.get(FontAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Switches to the previous font");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift LEFT"));

        updateState();
    }

    public void handleEvent(ProjectEvent event)
    {
        updateState();
    }

    public void handleEvent(FontEvent event)
    {
        updateState();
    }

    public void updateState()
    {
        Project project = projectAdapter.getProject();
        Font font = fontAdapter.getFont();

        setEnabled(project != null && font != null && project.indexOfFont(font) > 0);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Project project = projectAdapter.getProject();
        Font font = fontAdapter.getFont();
        int index = project.indexOfFont(font);

        fontAdapter.setFont(project.getFont(index - 1));
    }

}
