package org.cbm.editor.font.ui.main;

import javax.swing.JMenuBar;

public class MainMenuBarController
{

    private final MainMenuBar view;

    public MainMenuBarController()
    {
        super();

        view = new MainMenuBar();
    }

    public JMenuBar getView()
    {
        return view;
    }

}
