package org.cbm.editor.font.ui.main;

import javax.swing.JToolBar;

public class MainToolBarController
{

	private final MainToolBar view;

	public MainToolBarController()
	{
		super();

		view = new MainToolBar();
	}

	public JToolBar getView()
	{
		return view;
	}

}
