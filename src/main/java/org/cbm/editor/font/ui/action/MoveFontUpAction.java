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
import org.cbm.editor.font.model.events.FontSwitchedEvent;
import org.cbm.editor.font.model.events.ProjectSwitchedEvent;

public class MoveFontUpAction extends AbstractAction
{

    private static final long serialVersionUID = -754841748100014203L;

    private final ProjectAdapter projectAdapter;
    private final FontAdapter fontAdapter;

    public MoveFontUpAction()
    {
        super("Move Font Up", Icon.UP.getIcon());

        projectAdapter = Registry.get(ProjectAdapter.class).bind(this);
        fontAdapter = Registry.get(FontAdapter.class).bind(this);

        putValue(SHORT_DESCRIPTION, "Moves the selected font up one step");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl shift UP"));

        updateState();
    }

    private void updateState()
    {
        Font font = fontAdapter.getFont();
        Project project = projectAdapter.getProject();

        setEnabled(project != null && font != null && project.indexOfFont(font) > 0);
    }

    public void handleEvent(ProjectSwitchedEvent event)
    {
        updateState();
    }

    public void handleEvent(FontSwitchedEvent event)
    {
        updateState();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        Font font = fontAdapter.getFont();
        int index = projectAdapter.getProject().indexOfFont(font);

        Registry.execute(new MoveFontEdit(font, index - 1));
    }

}
