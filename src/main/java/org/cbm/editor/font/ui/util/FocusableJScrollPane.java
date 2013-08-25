package org.cbm.editor.font.ui.util;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

public class FocusableJScrollPane extends JScrollPane
{

	private static final long serialVersionUID = -4469534488194746018L;

	public FocusableJScrollPane()
	{
		super();
		setBorder(BorderFactory.createEmptyBorder());
		setFocusable(true);
	}

	public FocusableJScrollPane(final Component view, final int vsbPolicy, final int hsbPolicy)
	{
		super(view, vsbPolicy, hsbPolicy);
	}

	public FocusableJScrollPane(final Component view)
	{
		super(view);
	}

	public FocusableJScrollPane(final int vsbPolicy, final int hsbPolicy)
	{
		super(vsbPolicy, hsbPolicy);
	}

}
