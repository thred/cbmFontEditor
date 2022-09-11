package org.cbm.editor.font.ui.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.cbm.editor.font.Registry;
import org.cbm.editor.font.model.Project;
import org.cbm.editor.font.model.ProjectAdapter;
import org.cbm.editor.font.model.events.ProjectEvent;

public class MainFrameController
{

    private final ProjectAdapter projectAdapter;
    private final MainFrame view;

    public MainFrameController()
    {
        super();

        projectAdapter = Registry.get(ProjectAdapter.class);
        projectAdapter.bind(this);

        view = new MainFrame();

        updateState();

        view.addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(final WindowEvent e)
            {
                System.exit(0);
            }

        });
    }

    public JFrame getView()
    {
        return view;
    }

    public void handleEvent(final ProjectEvent event)
    {
        updateState();
    }

    private void updateState()
    {
        Project project = projectAdapter.getProject();

        if (project == null)
        {
            view.setTitle("CBM Font Editor");

            return;
        }

        String filename = project.getFile() != null ? project.getFile().toString() : "unnamed";

        if (project.isModified())
        {
            filename += "*";
        }

        view.setTitle("CBM Font Editor - " + filename);
    }

}
